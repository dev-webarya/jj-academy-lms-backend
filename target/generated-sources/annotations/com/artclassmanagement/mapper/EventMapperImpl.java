package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.EventRequestDto;
import com.artclassmanagement.dto.response.EventResponseDto;
import com.artclassmanagement.entity.Event;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-18T19:35:18+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public Event toEntity(EventRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Event.EventBuilder event = Event.builder();

        event.bannerUrl( dto.getBannerUrl() );
        event.description( dto.getDescription() );
        event.endDateTime( dto.getEndDateTime() );
        event.eventType( dto.getEventType() );
        event.fee( dto.getFee() );
        event.imageUrl( dto.getImageUrl() );
        List<String> list = dto.getInstructorIds();
        if ( list != null ) {
            event.instructorIds( new ArrayList<String>( list ) );
        }
        event.isOnline( dto.getIsOnline() );
        event.isPublic( dto.getIsPublic() );
        event.location( dto.getLocation() );
        event.maxParticipants( dto.getMaxParticipants() );
        event.meetingLink( dto.getMeetingLink() );
        event.meetingPassword( dto.getMeetingPassword() );
        event.startDateTime( dto.getStartDateTime() );
        event.title( dto.getTitle() );

        return event.build();
    }

    @Override
    public EventResponseDto toDto(Event entity) {
        if ( entity == null ) {
            return null;
        }

        EventResponseDto eventResponseDto = new EventResponseDto();

        eventResponseDto.setBannerUrl( entity.getBannerUrl() );
        eventResponseDto.setCreatedAt( entity.getCreatedAt() );
        eventResponseDto.setCreatedBy( entity.getCreatedBy() );
        eventResponseDto.setCurrentParticipants( entity.getCurrentParticipants() );
        eventResponseDto.setDescription( entity.getDescription() );
        eventResponseDto.setEndDateTime( entity.getEndDateTime() );
        eventResponseDto.setEventType( entity.getEventType() );
        eventResponseDto.setFee( entity.getFee() );
        eventResponseDto.setId( entity.getId() );
        eventResponseDto.setImageUrl( entity.getImageUrl() );
        List<String> list = entity.getInstructorIds();
        if ( list != null ) {
            eventResponseDto.setInstructorIds( new ArrayList<String>( list ) );
        }
        eventResponseDto.setIsOnline( entity.getIsOnline() );
        eventResponseDto.setIsPublic( entity.getIsPublic() );
        eventResponseDto.setIsRegistrationOpen( entity.getIsRegistrationOpen() );
        eventResponseDto.setLocation( entity.getLocation() );
        eventResponseDto.setMaxParticipants( entity.getMaxParticipants() );
        eventResponseDto.setMeetingLink( entity.getMeetingLink() );
        eventResponseDto.setMeetingPassword( entity.getMeetingPassword() );
        eventResponseDto.setStartDateTime( entity.getStartDateTime() );
        eventResponseDto.setTitle( entity.getTitle() );
        eventResponseDto.setUpdatedAt( entity.getUpdatedAt() );

        eventResponseDto.setHasAvailableSlots( entity.hasAvailableSlots() );

        return eventResponseDto;
    }

    @Override
    public void updateEntity(EventRequestDto dto, Event entity) {
        if ( dto == null ) {
            return;
        }

        entity.setBannerUrl( dto.getBannerUrl() );
        entity.setDescription( dto.getDescription() );
        entity.setEndDateTime( dto.getEndDateTime() );
        entity.setEventType( dto.getEventType() );
        entity.setFee( dto.getFee() );
        entity.setImageUrl( dto.getImageUrl() );
        if ( entity.getInstructorIds() != null ) {
            List<String> list = dto.getInstructorIds();
            if ( list != null ) {
                entity.getInstructorIds().clear();
                entity.getInstructorIds().addAll( list );
            }
            else {
                entity.setInstructorIds( null );
            }
        }
        else {
            List<String> list = dto.getInstructorIds();
            if ( list != null ) {
                entity.setInstructorIds( new ArrayList<String>( list ) );
            }
        }
        entity.setIsOnline( dto.getIsOnline() );
        entity.setIsPublic( dto.getIsPublic() );
        entity.setLocation( dto.getLocation() );
        entity.setMaxParticipants( dto.getMaxParticipants() );
        entity.setMeetingLink( dto.getMeetingLink() );
        entity.setMeetingPassword( dto.getMeetingPassword() );
        entity.setStartDateTime( dto.getStartDateTime() );
        entity.setTitle( dto.getTitle() );
    }
}
