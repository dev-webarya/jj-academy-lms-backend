package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.BatchRequestDto;
import com.artclassmanagement.dto.response.BatchResponseDto;
import com.artclassmanagement.entity.Batch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BatchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "instructorName", ignore = true)
    @Mapping(target = "currentStudentCount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Batch toEntity(BatchRequestDto dto);

    @Mapping(target = "hasAvailableSlots", expression = "java(entity.hasAvailableSlots())")
    BatchResponseDto toDto(Batch entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "instructorName", ignore = true)
    @Mapping(target = "currentStudentCount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(BatchRequestDto dto, @MappingTarget Batch entity);
}
