package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.entity.ClassSession;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-18T19:35:17+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ClassSessionMapperImpl implements ClassSessionMapper {

    @Override
    public ClassSession toEntity(ClassSessionRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        ClassSession.ClassSessionBuilder classSession = ClassSession.builder();

        classSession.batchId( dto.getBatchId() );
        classSession.description( dto.getDescription() );
        classSession.endTime( dto.getEndTime() );
        List<String> list = dto.getMaterials();
        if ( list != null ) {
            classSession.materials( new ArrayList<String>( list ) );
        }
        classSession.meetingLink( dto.getMeetingLink() );
        classSession.meetingPassword( dto.getMeetingPassword() );
        classSession.sessionDate( dto.getSessionDate() );
        classSession.startTime( dto.getStartTime() );
        classSession.topic( dto.getTopic() );

        return classSession.build();
    }

    @Override
    public ClassSessionResponseDto toDto(ClassSession entity) {
        if ( entity == null ) {
            return null;
        }

        ClassSessionResponseDto classSessionResponseDto = new ClassSessionResponseDto();

        classSessionResponseDto.setAbsentCount( entity.getAbsentCount() );
        classSessionResponseDto.setAttendanceTaken( entity.getAttendanceTaken() );
        classSessionResponseDto.setBatchId( entity.getBatchId() );
        classSessionResponseDto.setBatchName( entity.getBatchName() );
        classSessionResponseDto.setCancellationReason( entity.getCancellationReason() );
        classSessionResponseDto.setCreatedAt( entity.getCreatedAt() );
        classSessionResponseDto.setDescription( entity.getDescription() );
        classSessionResponseDto.setEndTime( entity.getEndTime() );
        classSessionResponseDto.setId( entity.getId() );
        classSessionResponseDto.setInstructorId( entity.getInstructorId() );
        classSessionResponseDto.setInstructorName( entity.getInstructorName() );
        List<String> list = entity.getMaterials();
        if ( list != null ) {
            classSessionResponseDto.setMaterials( new ArrayList<String>( list ) );
        }
        classSessionResponseDto.setMeetingLink( entity.getMeetingLink() );
        classSessionResponseDto.setMeetingPassword( entity.getMeetingPassword() );
        classSessionResponseDto.setPresentCount( entity.getPresentCount() );
        classSessionResponseDto.setRescheduledTo( entity.getRescheduledTo() );
        classSessionResponseDto.setSessionDate( entity.getSessionDate() );
        classSessionResponseDto.setStartTime( entity.getStartTime() );
        classSessionResponseDto.setStatus( entity.getStatus() );
        classSessionResponseDto.setTopic( entity.getTopic() );
        classSessionResponseDto.setTotalStudents( entity.getTotalStudents() );
        classSessionResponseDto.setUpdatedAt( entity.getUpdatedAt() );

        return classSessionResponseDto;
    }

    @Override
    public void updateEntity(ClassSessionRequestDto dto, ClassSession entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDescription( dto.getDescription() );
        entity.setEndTime( dto.getEndTime() );
        if ( entity.getMaterials() != null ) {
            List<String> list = dto.getMaterials();
            if ( list != null ) {
                entity.getMaterials().clear();
                entity.getMaterials().addAll( list );
            }
            else {
                entity.setMaterials( null );
            }
        }
        else {
            List<String> list = dto.getMaterials();
            if ( list != null ) {
                entity.setMaterials( new ArrayList<String>( list ) );
            }
        }
        entity.setMeetingLink( dto.getMeetingLink() );
        entity.setMeetingPassword( dto.getMeetingPassword() );
        entity.setSessionDate( dto.getSessionDate() );
        entity.setStartTime( dto.getStartTime() );
        entity.setTopic( dto.getTopic() );
    }
}
