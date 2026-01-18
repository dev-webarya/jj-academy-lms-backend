package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.entity.Attendance;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-18T19:35:18+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AttendanceMapperImpl implements AttendanceMapper {

    @Override
    public AttendanceResponseDto toDto(Attendance entity) {
        if ( entity == null ) {
            return null;
        }

        AttendanceResponseDto attendanceResponseDto = new AttendanceResponseDto();

        attendanceResponseDto.setBatchId( entity.getBatchId() );
        attendanceResponseDto.setCreatedAt( entity.getCreatedAt() );
        attendanceResponseDto.setId( entity.getId() );
        attendanceResponseDto.setLateByMinutes( entity.getLateByMinutes() );
        attendanceResponseDto.setMarkedAt( entity.getMarkedAt() );
        attendanceResponseDto.setMarkedBy( entity.getMarkedBy() );
        attendanceResponseDto.setMarkedByName( entity.getMarkedByName() );
        attendanceResponseDto.setRemarks( entity.getRemarks() );
        attendanceResponseDto.setSessionId( entity.getSessionId() );
        attendanceResponseDto.setStatus( entity.getStatus() );
        attendanceResponseDto.setStudentEmail( entity.getStudentEmail() );
        attendanceResponseDto.setStudentId( entity.getStudentId() );
        attendanceResponseDto.setStudentName( entity.getStudentName() );

        return attendanceResponseDto;
    }
}
