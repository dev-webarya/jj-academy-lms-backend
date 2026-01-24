package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.entity.Attendance;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T16:54:48+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AttendanceMapperImpl implements AttendanceMapper {

    @Override
    public AttendanceResponseDto toDto(Attendance entity) {
        if ( entity == null ) {
            return null;
        }

        AttendanceResponseDto.AttendanceResponseDtoBuilder attendanceResponseDto = AttendanceResponseDto.builder();

        attendanceResponseDto.createdAt( entity.getCreatedAt() );
        attendanceResponseDto.id( entity.getId() );
        attendanceResponseDto.isOverLimit( entity.getIsOverLimit() );
        attendanceResponseDto.markedAt( entity.getMarkedAt() );
        attendanceResponseDto.remarks( entity.getRemarks() );
        attendanceResponseDto.sessionCountThisMonth( entity.getSessionCountThisMonth() );
        attendanceResponseDto.sessionDate( entity.getSessionDate() );
        attendanceResponseDto.sessionId( entity.getSessionId() );
        attendanceResponseDto.sessionMonth( entity.getSessionMonth() );
        attendanceResponseDto.sessionYear( entity.getSessionYear() );
        attendanceResponseDto.status( entity.getStatus() );
        attendanceResponseDto.studentEmail( entity.getStudentEmail() );
        attendanceResponseDto.studentId( entity.getStudentId() );
        attendanceResponseDto.studentName( entity.getStudentName() );
        attendanceResponseDto.subscriptionId( entity.getSubscriptionId() );

        return attendanceResponseDto.build();
    }
}
