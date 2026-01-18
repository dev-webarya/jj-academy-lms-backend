package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.EnrollmentResponseDto;
import com.artclassmanagement.entity.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    EnrollmentResponseDto toDto(Enrollment entity);
}
