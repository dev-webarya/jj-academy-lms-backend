package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.SubscriptionResponseDto;
import com.artclassmanagement.entity.StudentSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "remainingSessions", expression = "java(entity.getRemainingSessionCount())")
    @Mapping(target = "isOverLimit", expression = "java(entity.isOverLimit())")
    SubscriptionResponseDto toDto(StudentSubscription entity);
}
