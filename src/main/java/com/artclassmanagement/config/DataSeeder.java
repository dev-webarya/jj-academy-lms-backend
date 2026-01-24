package com.artclassmanagement.config;

import com.artclassmanagement.entity.*;
import com.artclassmanagement.enums.*;
import com.artclassmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Data Seeder for LMS - Creates sample data for testing.
 * Simplified model: Admin manages everything, students have monthly
 * subscriptions with 8-class limit.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

        private final UserRepository userRepository;
        private final StudentSubscriptionRepository subscriptionRepository;
        private final ClassSessionRepository classSessionRepository;
        private final ClassEnrollmentRepository classEnrollmentRepository;
        private final EventRepository eventRepository;
        private final GalleryItemRepository galleryItemRepository;
        private final AttendanceRepository attendanceRepository;
        private final PasswordEncoder passwordEncoder;

        private static final String ADMIN_EMAIL = "admin@artacademy.com";
        private static final String DEFAULT_PASSWORD = "password123";

        @Override
        public void run(String... args) throws Exception {
                // Only seed if no subscriptions exist (to avoid duplicate seeds)
                if (subscriptionRepository.count() > 0) {
                        log.info("LMS data already seeded. Skipping...");
                        return;
                }

                log.info("========== Starting LMS Data Seeding ==========");

                // 1. Create Admin User
                User admin = createUser("Admin", "User", ADMIN_EMAIL, "+1000000000",
                                Set.of("ROLE_ADMIN", "ROLE_CUSTOMER"));
                log.info("Admin created: {} (ID: {})", admin.getEmail(), admin.getId());

                // 2. Create 6 Students
                List<User> students = new ArrayList<>();
                String[][] studentData = {
                                { "Sarah", "Johnson", "sarah.johnson@example.com", "+1234567001" },
                                { "Michael", "Chen", "michael.chen@example.com", "+1234567002" },
                                { "Emily", "Williams", "emily.williams@example.com", "+1234567003" },
                                { "David", "Brown", "david.brown@example.com", "+1234567004" },
                                { "Jessica", "Garcia", "jessica.garcia@example.com", "+1234567005" },
                                { "James", "Martinez", "james.martinez@example.com", "+1234567006" }
                };
                for (String[] data : studentData) {
                        User student = createUser(data[0], data[1], data[2], data[3], Set.of("ROLE_CUSTOMER"));
                        students.add(student);
                }
                log.info("Created {} students", students.size());

                // 3. Create Monthly Subscriptions for all students (current month)
                LocalDate now = LocalDate.now();
                int currentMonth = now.getMonthValue();
                int currentYear = now.getYear();
                LocalDate monthStart = now.withDayOfMonth(1);
                LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());

                List<StudentSubscription> subscriptions = new ArrayList<>();
                int[] attendedCounts = { 3, 5, 2, 7, 8, 9 }; // Last student is over limit!

                for (int i = 0; i < students.size(); i++) {
                        User student = students.get(i);
                        StudentSubscription subscription = StudentSubscription.builder()
                                        .studentId(student.getId())
                                        .studentName(student.getFullName())
                                        .studentEmail(student.getEmail())
                                        .studentPhone(student.getPhoneNumber())
                                        .subscriptionMonth(currentMonth)
                                        .subscriptionYear(currentYear)
                                        .startDate(monthStart)
                                        .endDate(monthEnd)
                                        .allowedSessions(8)
                                        .attendedSessions(attendedCounts[i])
                                        .status(SubscriptionStatus.ACTIVE)
                                        .amountPaid(2000.0)
                                        .notes(attendedCounts[i] > 8 ? "Over limit - exceeded 8 sessions"
                                                        : attendedCounts[i] == 8 ? "Limit reached" : null)
                                        .build();
                        subscriptions.add(subscriptionRepository.save(subscription));
                }
                log.info("Created {} subscriptions for current month ({}/{})", subscriptions.size(), currentMonth,
                                currentYear);

                // 4. Create 8 Class Sessions for the month (runs 4 days/week = ~16/month,
                // sample 8)
                String[] topics = {
                                "Oil Painting Basics",
                                "Watercolor Techniques",
                                "Sculpture Fundamentals",
                                "Portrait Drawing",
                                "Landscape Art",
                                "Abstract Expression",
                                "Color Theory",
                                "Mixed Media Art"
                };

                List<ClassSession> sessions = new ArrayList<>();
                LocalDate sessionDate = monthStart.plusDays(1); // Start from 2nd day

                for (int i = 0; i < 8; i++) {
                        // Skip weekends
                        while (sessionDate.getDayOfWeek().getValue() > 5) {
                                sessionDate = sessionDate.plusDays(1);
                        }

                        boolean isCompleted = i < 6; // First 6 sessions are completed

                        ClassSession session = ClassSession.builder()
                                        .sessionDate(sessionDate)
                                        .startTime(LocalTime.of(10, 0))
                                        .endTime(LocalTime.of(12, 0))
                                        .topic(topics[i])
                                        .description("Learn " + topics[i].toLowerCase() + " in this hands-on session.")
                                        .meetingLink("https://meet.example.com/session-" + (i + 1))
                                        .meetingPassword("art123")
                                        .status(isCompleted ? SessionStatus.COMPLETED : SessionStatus.SCHEDULED)
                                        .totalStudents(isCompleted ? 6 : 0)
                                        .presentCount(isCompleted ? 5 : 0)
                                        .absentCount(isCompleted ? 1 : 0)
                                        .attendanceTaken(isCompleted)
                                        .build();

                        sessions.add(classSessionRepository.save(session));
                        sessionDate = sessionDate.plusDays(2); // Every other day
                }
                log.info("Created {} class sessions", sessions.size());

                // 5. Create Attendance Records for completed sessions
                for (ClassSession session : sessions) {
                        if (Boolean.TRUE.equals(session.getAttendanceTaken())) {
                                List<ClassSession.SessionAttendanceRecord> sessionRecords = new ArrayList<>();

                                for (int i = 0; i < students.size(); i++) {
                                        User student = students.get(i);
                                        StudentSubscription sub = subscriptions.get(i);

                                        boolean isPresent = i != 5; // Last student is always absent
                                        AttendanceStatus status = isPresent ? AttendanceStatus.PRESENT
                                                        : AttendanceStatus.ABSENT;

                                        Attendance attendance = Attendance.builder()
                                                        .sessionId(session.getId())
                                                        .studentId(student.getId())
                                                        .studentName(student.getFullName())
                                                        .studentEmail(student.getEmail())
                                                        .subscriptionId(sub.getId())
                                                        .sessionDate(session.getSessionDate())
                                                        .sessionMonth(currentMonth)
                                                        .sessionYear(currentYear)
                                                        .sessionCountThisMonth(sub.getAttendedSessions())
                                                        .isOverLimit(sub.getAttendedSessions() > 8)
                                                        .status(status)
                                                        .markedAt(Instant.now())
                                                        .remarks(isPresent ? "On time" : "Did not attend")
                                                        .build();
                                        attendanceRepository.save(attendance);

                                        // Add to session's embedded records
                                        ClassSession.SessionAttendanceRecord record = ClassSession.SessionAttendanceRecord
                                                        .builder()
                                                        .studentId(student.getId())
                                                        .studentName(student.getFullName())
                                                        .studentEmail(student.getEmail())
                                                        .subscriptionId(sub.getId())
                                                        .isPresent(isPresent)
                                                        .sessionCountThisMonth(sub.getAttendedSessions())
                                                        .isOverLimit(sub.getAttendedSessions() > 8)
                                                        .remarks(isPresent ? "On time" : "Did not attend")
                                                        .markedAt(Instant.now())
                                                        .build();
                                        sessionRecords.add(record);
                                }

                                session.setAttendanceRecords(sessionRecords);
                                classSessionRepository.save(session);
                        }
                }
                log.info("Created attendance records for completed sessions");

                // 6. Create 3 Events
                Event event1 = Event.builder()
                                .title("Annual Art Exhibition 2026")
                                .description("Showcase your artwork at our premier annual exhibition.")
                                .eventType(EventType.EXHIBITION)
                                .startDateTime(Instant.parse("2026-03-15T04:30:00Z"))
                                .endDateTime(Instant.parse("2026-03-17T12:30:00Z"))
                                .location("Art Academy Main Hall")
                                .isOnline(false)
                                .imageUrl("https://example.com/events/exhibition-2026.jpg")
                                .bannerUrl("https://example.com/events/exhibition-2026-banner.jpg")
                                .maxParticipants(100)
                                .currentParticipants(45)
                                .isPublic(true)
                                .isRegistrationOpen(true)
                                .fee(0.0)
                                .createdBy(admin.getId())
                                .build();
                eventRepository.save(event1);

                Event event2 = Event.builder()
                                .title("Oil Painting Workshop")
                                .description("Intensive one-day workshop covering advanced oil painting techniques.")
                                .eventType(EventType.WORKSHOP)
                                .startDateTime(Instant.parse("2026-02-20T03:30:00Z"))
                                .endDateTime(Instant.parse("2026-02-20T11:30:00Z"))
                                .location("Studio A")
                                .isOnline(false)
                                .imageUrl("https://example.com/events/oil-workshop.jpg")
                                .maxParticipants(20)
                                .currentParticipants(12)
                                .isPublic(true)
                                .isRegistrationOpen(true)
                                .fee(1500.0)
                                .createdBy(admin.getId())
                                .build();
                eventRepository.save(event2);

                Event event3 = Event.builder()
                                .title("Digital Art Webinar")
                                .description("Free online webinar introducing digital art tools for beginners.")
                                .eventType(EventType.WEBINAR)
                                .startDateTime(Instant.parse("2026-02-25T12:30:00Z"))
                                .endDateTime(Instant.parse("2026-02-25T14:30:00Z"))
                                .location("Online")
                                .isOnline(true)
                                .meetingLink("https://meet.example.com/digital-art-webinar")
                                .meetingPassword("digital2026")
                                .imageUrl("https://example.com/events/digital-webinar.jpg")
                                .maxParticipants(200)
                                .currentParticipants(87)
                                .isPublic(true)
                                .isRegistrationOpen(true)
                                .fee(0.0)
                                .createdBy(admin.getId())
                                .build();
                eventRepository.save(event3);
                log.info("Created 3 events");

                // 7. Create 6 Gallery Items
                VerificationStatus[] galleryStatuses = {
                                VerificationStatus.APPROVED, VerificationStatus.APPROVED, VerificationStatus.APPROVED,
                                VerificationStatus.APPROVED, VerificationStatus.PENDING, VerificationStatus.PENDING
                };

                String[][] galleryData = {
                                { "Sunset Over Mountains", "Oil painting capturing a vibrant sunset" },
                                { "Portrait Study", "Classical portrait using traditional techniques" },
                                { "Ocean Waves", "Watercolor seascape with soft morning light" },
                                { "Spring Garden", "Vibrant watercolor of a blooming garden" },
                                { "Abstract Emotions", "Mixed media abstract exploring color" },
                                { "City Lights", "Urban nightscape with dramatic lighting" }
                };

                for (int i = 0; i < galleryData.length; i++) {
                        User student = students.get(i);
                        boolean isApproved = galleryStatuses[i] == VerificationStatus.APPROVED;

                        GalleryItem item = GalleryItem.builder()
                                        .uploadedBy(student.getId())
                                        .uploaderName(student.getFullName())
                                        .uploaderRole("STUDENT")
                                        .title(galleryData[i][0])
                                        .description(galleryData[i][1])
                                        .mediaUrl("https://example.com/gallery/artwork-" + (i + 1) + ".jpg")
                                        .mediaType(MediaType.IMAGE)
                                        .thumbnailUrl("https://example.com/gallery/artwork-" + (i + 1) + "-thumb.jpg")
                                        .tags(Arrays.asList("student-work", "2026"))
                                        .verificationStatus(galleryStatuses[i])
                                        .verifiedBy(isApproved ? admin.getId() : null)
                                        .verifiedByName(isApproved ? admin.getFullName() : null)
                                        .verifiedAt(isApproved ? Instant.now().minusSeconds(86400) : null)
                                        .isPublic(isApproved)
                                        .isFeatured(i < 2)
                                        .viewCount(isApproved ? 50 + i * 10 : 0)
                                        .likeCount(isApproved ? 10 + i * 5 : 0)
                                        .build();
                        galleryItemRepository.save(item);
                }
                log.info("Created 6 gallery items");

                // Print Summary
                log.info("========== LMS Data Seeding Complete ==========");
                log.info("");
                log.info("╔══════════════════════════════════════════════╗");
                log.info("║           SEED DATA SUMMARY                  ║");
                log.info("╠══════════════════════════════════════════════╣");
                log.info("║ ADMIN ACCOUNT (password: {})       ║", DEFAULT_PASSWORD);
                log.info("║   Email: {}           ║", ADMIN_EMAIL);
                log.info("╠══════════════════════════════════════════════╣");
                log.info("║ STUDENTS (password: {})            ║", DEFAULT_PASSWORD);
                for (int i = 0; i < students.size(); i++) {
                        log.info("║   {}: {}  ║", i + 1, students.get(i).getEmail());
                }
                log.info("╠══════════════════════════════════════════════╣");
                log.info("║ SUBSCRIPTIONS                                ║");
                log.info("║   - All 6 students: Current month            ║");
                log.info("║   - 8 sessions allowed per month             ║");
                log.info("║   - 1 student over limit (9 sessions)        ║");
                log.info("╠══════════════════════════════════════════════╣");
                log.info("║ ENTITIES CREATED                             ║");
                log.info("║   - Subscriptions: 6                         ║");
                log.info("║   - Sessions: 8 (6 completed, 2 scheduled)   ║");
                log.info("║   - Attendance Records: 36                   ║");
                log.info("║   - Events: 3                                ║");
                log.info("║   - Gallery Items: 6                         ║");
                log.info("╚══════════════════════════════════════════════╝");
        }

        private User createUser(String firstName, String lastName, String email, String phone, Set<String> roles) {
                return userRepository.findByEmail(email)
                                .orElseGet(() -> {
                                        User user = User.builder()
                                                        .firstName(firstName)
                                                        .lastName(lastName)
                                                        .email(email)
                                                        .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                                                        .phoneNumber(phone)
                                                        .roles(roles)
                                                        .isEnabled(true)
                                                        .deleted(false)
                                                        .build();
                                        return userRepository.save(user);
                                });
        }
}
