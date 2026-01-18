package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.EnrollmentResponseDto;
import com.artclassmanagement.entity.Enrollment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-18T19:35:18+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class EnrollmentMapperImpl implements EnrollmentMapper {

    @Override
    public EnrollmentResponseDto toDto(Enrollment entity) {
        if ( entity == null ) {
            return null;
        }

        EnrollmentResponseDto enrollmentResponseDto = new EnrollmentResponseDto();

        enrollmentResponseDto.setAssignedToBatchDate( entity.getAssignedToBatchDate() );
        enrollmentResponseDto.setAttendancePercentage( entity.getAttendancePercentage() );
        enrollmentResponseDto.setBatchId( entity.getBatchId() );
        enrollmentResponseDto.setBatchName( entity.getBatchName() );
        enrollmentResponseDto.setClassId( entity.getClassId() );
        enrollmentResponseDto.setClassName( entity.getClassName() );
        enrollmentResponseDto.setCreatedAt( entity.getCreatedAt() );
        enrollmentResponseDto.setEnrollmentDate( entity.getEnrollmentDate() );
        enrollmentResponseDto.setId( entity.getId() );
        enrollmentResponseDto.setNotes( entity.getNotes() );
        enrollmentResponseDto.setPaymentId( entity.getPaymentId() );
        enrollmentResponseDto.setSessionsAttended( entity.getSessionsAttended() );
        enrollmentResponseDto.setStatus( entity.getStatus() );
        enrollmentResponseDto.setStudentEmail( entity.getStudentEmail() );
        enrollmentResponseDto.setStudentName( entity.getStudentName() );
        enrollmentResponseDto.setTotalSessions( entity.getTotalSessions() );
        enrollmentResponseDto.setUpdatedAt( entity.getUpdatedAt() );
        enrollmentResponseDto.setUserId( entity.getUserId() );

        return enrollmentResponseDto;
    }
}
