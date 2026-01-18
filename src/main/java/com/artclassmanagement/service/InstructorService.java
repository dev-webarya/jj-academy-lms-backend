package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.InstructorRequestDto;
import com.artclassmanagement.dto.response.InstructorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstructorService {
    InstructorResponseDto create(InstructorRequestDto request);

    InstructorResponseDto getById(String id);

    InstructorResponseDto getByUserId(String userId);

    Page<InstructorResponseDto> getAll(Pageable pageable);

    Page<InstructorResponseDto> getActive(Pageable pageable);

    InstructorResponseDto update(String id, InstructorRequestDto request);

    InstructorResponseDto toggleActive(String id, Boolean isActive);

    void delete(String id);
}
