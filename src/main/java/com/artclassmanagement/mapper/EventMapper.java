package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.EventRequestDto;
import com.artclassmanagement.dto.response.EventResponseDto;
import com.artclassmanagement.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentParticipants", ignore = true)
    @Mapping(target = "isRegistrationOpen", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toEntity(EventRequestDto dto);

    @Mapping(target = "hasAvailableSlots", expression = "java(entity.hasAvailableSlots())")
    EventResponseDto toDto(Event entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentParticipants", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(EventRequestDto dto, @MappingTarget Event entity);
}
