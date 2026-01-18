package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.entity.ClassSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClassSessionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "batchName", ignore = true)
    @Mapping(target = "instructorId", ignore = true)
    @Mapping(target = "instructorName", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "rescheduledTo", ignore = true)
    @Mapping(target = "totalStudents", ignore = true)
    @Mapping(target = "presentCount", ignore = true)
    @Mapping(target = "absentCount", ignore = true)
    @Mapping(target = "attendanceTaken", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClassSession toEntity(ClassSessionRequestDto dto);

    ClassSessionResponseDto toDto(ClassSession entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "batchName", ignore = true)
    @Mapping(target = "instructorId", ignore = true)
    @Mapping(target = "instructorName", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ClassSessionRequestDto dto, @MappingTarget ClassSession entity);
}
