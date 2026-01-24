package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.entity.ClassSession;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T16:54:47+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ClassSessionMapperImpl implements ClassSessionMapper {

    @Override
    public ClassSession toEntity(ClassSessionRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        ClassSession.ClassSessionBuilder classSession = ClassSession.builder();

        classSession.description( dto.getDescription() );
        classSession.endTime( dto.getEndTime() );
        classSession.meetingLink( dto.getMeetingLink() );
        classSession.meetingPassword( dto.getMeetingPassword() );
        classSession.sessionDate( dto.getSessionDate() );
        classSession.startTime( dto.getStartTime() );
        classSession.topic( dto.getTopic() );

        return classSession.build();
    }

    @Override
    public ClassSessionResponseDto toDto(ClassSession entity) {
        if ( entity == null ) {
            return null;
        }

        ClassSessionResponseDto.ClassSessionResponseDtoBuilder classSessionResponseDto = ClassSessionResponseDto.builder();

        classSessionResponseDto.absentCount( entity.getAbsentCount() );
        classSessionResponseDto.attendanceRecords( sessionAttendanceRecordListToAttendanceRecordDtoList( entity.getAttendanceRecords() ) );
        classSessionResponseDto.attendanceTaken( entity.getAttendanceTaken() );
        classSessionResponseDto.attendanceTakenAt( entity.getAttendanceTakenAt() );
        classSessionResponseDto.cancellationReason( entity.getCancellationReason() );
        classSessionResponseDto.createdAt( entity.getCreatedAt() );
        classSessionResponseDto.description( entity.getDescription() );
        classSessionResponseDto.endTime( entity.getEndTime() );
        classSessionResponseDto.id( entity.getId() );
        classSessionResponseDto.meetingLink( entity.getMeetingLink() );
        classSessionResponseDto.meetingPassword( entity.getMeetingPassword() );
        classSessionResponseDto.presentCount( entity.getPresentCount() );
        classSessionResponseDto.sessionDate( entity.getSessionDate() );
        classSessionResponseDto.startTime( entity.getStartTime() );
        classSessionResponseDto.status( entity.getStatus() );
        classSessionResponseDto.topic( entity.getTopic() );
        classSessionResponseDto.totalStudents( entity.getTotalStudents() );
        classSessionResponseDto.updatedAt( entity.getUpdatedAt() );

        return classSessionResponseDto.build();
    }

    @Override
    public void updateEntity(ClassSessionRequestDto dto, ClassSession entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDescription( dto.getDescription() );
        entity.setEndTime( dto.getEndTime() );
        entity.setMeetingLink( dto.getMeetingLink() );
        entity.setMeetingPassword( dto.getMeetingPassword() );
        entity.setSessionDate( dto.getSessionDate() );
        entity.setStartTime( dto.getStartTime() );
        entity.setTopic( dto.getTopic() );
    }

    protected ClassSessionResponseDto.AttendanceRecordDto sessionAttendanceRecordToAttendanceRecordDto(ClassSession.SessionAttendanceRecord sessionAttendanceRecord) {
        if ( sessionAttendanceRecord == null ) {
            return null;
        }

        ClassSessionResponseDto.AttendanceRecordDto.AttendanceRecordDtoBuilder attendanceRecordDto = ClassSessionResponseDto.AttendanceRecordDto.builder();

        attendanceRecordDto.isOverLimit( sessionAttendanceRecord.getIsOverLimit() );
        attendanceRecordDto.isPresent( sessionAttendanceRecord.getIsPresent() );
        attendanceRecordDto.markedAt( sessionAttendanceRecord.getMarkedAt() );
        attendanceRecordDto.remarks( sessionAttendanceRecord.getRemarks() );
        attendanceRecordDto.sessionCountThisMonth( sessionAttendanceRecord.getSessionCountThisMonth() );
        attendanceRecordDto.studentEmail( sessionAttendanceRecord.getStudentEmail() );
        attendanceRecordDto.studentId( sessionAttendanceRecord.getStudentId() );
        attendanceRecordDto.studentName( sessionAttendanceRecord.getStudentName() );
        attendanceRecordDto.subscriptionId( sessionAttendanceRecord.getSubscriptionId() );

        return attendanceRecordDto.build();
    }

    protected List<ClassSessionResponseDto.AttendanceRecordDto> sessionAttendanceRecordListToAttendanceRecordDtoList(List<ClassSession.SessionAttendanceRecord> list) {
        if ( list == null ) {
            return null;
        }

        List<ClassSessionResponseDto.AttendanceRecordDto> list1 = new ArrayList<ClassSessionResponseDto.AttendanceRecordDto>( list.size() );
        for ( ClassSession.SessionAttendanceRecord sessionAttendanceRecord : list ) {
            list1.add( sessionAttendanceRecordToAttendanceRecordDto( sessionAttendanceRecord ) );
        }

        return list1;
    }
}
