package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.BatchRequestDto;
import com.artclassmanagement.dto.response.BatchResponseDto;
import com.artclassmanagement.entity.Batch;
import com.artclassmanagement.enums.DayOfWeekEnum;
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
public class BatchMapperImpl implements BatchMapper {

    @Override
    public Batch toEntity(BatchRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Batch.BatchBuilder batch = Batch.builder();

        batch.batchCode( dto.getBatchCode() );
        batch.batchName( dto.getBatchName() );
        List<DayOfWeekEnum> list = dto.getClassDays();
        if ( list != null ) {
            batch.classDays( new ArrayList<DayOfWeekEnum>( list ) );
        }
        batch.classId( dto.getClassId() );
        batch.defaultMeetingLink( dto.getDefaultMeetingLink() );
        batch.description( dto.getDescription() );
        batch.endDate( dto.getEndDate() );
        batch.endTime( dto.getEndTime() );
        batch.instructorId( dto.getInstructorId() );
        batch.location( dto.getLocation() );
        batch.maxStudents( dto.getMaxStudents() );
        batch.startDate( dto.getStartDate() );
        batch.startTime( dto.getStartTime() );
        batch.timezone( dto.getTimezone() );

        return batch.build();
    }

    @Override
    public BatchResponseDto toDto(Batch entity) {
        if ( entity == null ) {
            return null;
        }

        BatchResponseDto batchResponseDto = new BatchResponseDto();

        batchResponseDto.setBatchCode( entity.getBatchCode() );
        batchResponseDto.setBatchName( entity.getBatchName() );
        List<DayOfWeekEnum> list = entity.getClassDays();
        if ( list != null ) {
            batchResponseDto.setClassDays( new ArrayList<DayOfWeekEnum>( list ) );
        }
        batchResponseDto.setClassId( entity.getClassId() );
        batchResponseDto.setClassName( entity.getClassName() );
        batchResponseDto.setCreatedAt( entity.getCreatedAt() );
        batchResponseDto.setCurrentStudentCount( entity.getCurrentStudentCount() );
        batchResponseDto.setDefaultMeetingLink( entity.getDefaultMeetingLink() );
        batchResponseDto.setDescription( entity.getDescription() );
        batchResponseDto.setEndDate( entity.getEndDate() );
        batchResponseDto.setEndTime( entity.getEndTime() );
        batchResponseDto.setId( entity.getId() );
        batchResponseDto.setInstructorId( entity.getInstructorId() );
        batchResponseDto.setInstructorName( entity.getInstructorName() );
        batchResponseDto.setLocation( entity.getLocation() );
        batchResponseDto.setMaxStudents( entity.getMaxStudents() );
        batchResponseDto.setStartDate( entity.getStartDate() );
        batchResponseDto.setStartTime( entity.getStartTime() );
        batchResponseDto.setStatus( entity.getStatus() );
        batchResponseDto.setTimezone( entity.getTimezone() );
        batchResponseDto.setUpdatedAt( entity.getUpdatedAt() );

        batchResponseDto.setHasAvailableSlots( entity.hasAvailableSlots() );

        return batchResponseDto;
    }

    @Override
    public void updateEntity(BatchRequestDto dto, Batch entity) {
        if ( dto == null ) {
            return;
        }

        entity.setBatchCode( dto.getBatchCode() );
        entity.setBatchName( dto.getBatchName() );
        if ( entity.getClassDays() != null ) {
            List<DayOfWeekEnum> list = dto.getClassDays();
            if ( list != null ) {
                entity.getClassDays().clear();
                entity.getClassDays().addAll( list );
            }
            else {
                entity.setClassDays( null );
            }
        }
        else {
            List<DayOfWeekEnum> list = dto.getClassDays();
            if ( list != null ) {
                entity.setClassDays( new ArrayList<DayOfWeekEnum>( list ) );
            }
        }
        entity.setClassId( dto.getClassId() );
        entity.setDefaultMeetingLink( dto.getDefaultMeetingLink() );
        entity.setDescription( dto.getDescription() );
        entity.setEndDate( dto.getEndDate() );
        entity.setEndTime( dto.getEndTime() );
        entity.setInstructorId( dto.getInstructorId() );
        entity.setLocation( dto.getLocation() );
        entity.setMaxStudents( dto.getMaxStudents() );
        entity.setStartDate( dto.getStartDate() );
        entity.setStartTime( dto.getStartTime() );
        entity.setTimezone( dto.getTimezone() );
    }
}
