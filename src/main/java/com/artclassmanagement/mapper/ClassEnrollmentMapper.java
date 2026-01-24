package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.ClassEnrollmentResponseDto;
import com.artclassmanagement.entity.ClassEnrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassEnrollmentMapper {
    ClassEnrollmentResponseDto toDto(ClassEnrollment entity);
}
