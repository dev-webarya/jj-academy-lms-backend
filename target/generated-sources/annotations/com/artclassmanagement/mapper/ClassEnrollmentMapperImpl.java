package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.ClassEnrollmentResponseDto;
import com.artclassmanagement.entity.ClassEnrollment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T16:54:47+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ClassEnrollmentMapperImpl implements ClassEnrollmentMapper {

    @Override
    public ClassEnrollmentResponseDto toDto(ClassEnrollment entity) {
        if ( entity == null ) {
            return null;
        }

        ClassEnrollmentResponseDto classEnrollmentResponseDto = new ClassEnrollmentResponseDto();

        classEnrollmentResponseDto.setAdditionalMessage( entity.getAdditionalMessage() );
        classEnrollmentResponseDto.setAdminNotes( entity.getAdminNotes() );
        classEnrollmentResponseDto.setClassDescription( entity.getClassDescription() );
        classEnrollmentResponseDto.setClassId( entity.getClassId() );
        classEnrollmentResponseDto.setClassName( entity.getClassName() );
        classEnrollmentResponseDto.setCreatedAt( entity.getCreatedAt() );
        classEnrollmentResponseDto.setId( entity.getId() );
        classEnrollmentResponseDto.setParentGuardianName( entity.getParentGuardianName() );
        classEnrollmentResponseDto.setSchedule( entity.getSchedule() );
        classEnrollmentResponseDto.setStatus( entity.getStatus() );
        classEnrollmentResponseDto.setStudentAge( entity.getStudentAge() );
        classEnrollmentResponseDto.setStudentEmail( entity.getStudentEmail() );
        classEnrollmentResponseDto.setStudentName( entity.getStudentName() );
        classEnrollmentResponseDto.setStudentPhone( entity.getStudentPhone() );
        classEnrollmentResponseDto.setUpdatedAt( entity.getUpdatedAt() );
        classEnrollmentResponseDto.setUserId( entity.getUserId() );

        return classEnrollmentResponseDto;
    }
}
