package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.InstructorRequestDto;
import com.artclassmanagement.dto.response.InstructorResponseDto;
import com.artclassmanagement.entity.Instructor;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.exception.DuplicateResourceException;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.InstructorMapper;
import com.artclassmanagement.repository.InstructorRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final InstructorMapper instructorMapper;

    @Override
    public InstructorResponseDto create(InstructorRequestDto request) {
        log.info("Creating instructor profile for user: {}", request.getUserId());

        if (instructorRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("Instructor profile already exists for this user");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Instructor instructor = instructorMapper.toEntity(request);
        instructor.setEmail(user.getEmail());
        instructor.setPhoneNumber(user.getPhoneNumber());
        instructor.setDisplayName(request.getDisplayName() != null ? request.getDisplayName() : user.getFullName());
        instructor.setIsActive(true);
        instructor.setIsDeleted(false);

        // Add ROLE_INSTRUCTOR to user if not present
        if (!user.getRoles().contains("ROLE_INSTRUCTOR")) {
            user.getRoles().add("ROLE_INSTRUCTOR");
            userRepository.save(user);
        }

        return instructorMapper.toDto(instructorRepository.save(instructor));
    }

    @Override
    public InstructorResponseDto getById(String id) {
        return instructorMapper.toDto(findById(id));
    }

    @Override
    public InstructorResponseDto getByUserId(String userId) {
        Instructor instructor = instructorRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "userId", userId));
        return instructorMapper.toDto(instructor);
    }

    @Override
    public Page<InstructorResponseDto> getAll(Pageable pageable) {
        return instructorRepository.findByIsDeletedFalse(pageable).map(instructorMapper::toDto);
    }

    @Override
    public Page<InstructorResponseDto> getActive(Pageable pageable) {
        return instructorRepository.findByIsActiveAndIsDeletedFalse(true, pageable).map(instructorMapper::toDto);
    }

    @Override
    public InstructorResponseDto update(String id, InstructorRequestDto request) {
        log.info("Updating instructor: {}", id);
        Instructor instructor = findById(id);
        instructorMapper.updateEntity(request, instructor);
        return instructorMapper.toDto(instructorRepository.save(instructor));
    }

    @Override
    public InstructorResponseDto toggleActive(String id, Boolean isActive) {
        log.info("Setting instructor {} active status to: {}", id, isActive);
        Instructor instructor = findById(id);
        instructor.setIsActive(isActive);
        return instructorMapper.toDto(instructorRepository.save(instructor));
    }

    @Override
    public void delete(String id) {
        log.warn("Soft deleting instructor: {}", id);
        Instructor instructor = findById(id);
        instructor.setIsDeleted(true);
        instructor.setIsActive(false);
        instructorRepository.save(instructor);
    }

    private Instructor findById(String id) {
        return instructorRepository.findById(id)
                .filter(i -> !i.getIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", id));
    }
}
