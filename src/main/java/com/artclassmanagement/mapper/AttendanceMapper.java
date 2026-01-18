package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.entity.Attendance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    AttendanceResponseDto toDto(Attendance entity);
}
