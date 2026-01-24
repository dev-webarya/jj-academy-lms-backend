package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.response.SubscriptionResponseDto;
import com.artclassmanagement.entity.StudentSubscription;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T16:54:48+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Override
    public SubscriptionResponseDto toDto(StudentSubscription entity) {
        if ( entity == null ) {
            return null;
        }

        SubscriptionResponseDto.SubscriptionResponseDtoBuilder subscriptionResponseDto = SubscriptionResponseDto.builder();

        subscriptionResponseDto.allowedSessions( entity.getAllowedSessions() );
        subscriptionResponseDto.amountPaid( entity.getAmountPaid() );
        subscriptionResponseDto.attendedSessions( entity.getAttendedSessions() );
        subscriptionResponseDto.createdAt( entity.getCreatedAt() );
        subscriptionResponseDto.endDate( entity.getEndDate() );
        subscriptionResponseDto.id( entity.getId() );
        subscriptionResponseDto.notes( entity.getNotes() );
        subscriptionResponseDto.paymentId( entity.getPaymentId() );
        subscriptionResponseDto.startDate( entity.getStartDate() );
        subscriptionResponseDto.status( entity.getStatus() );
        subscriptionResponseDto.studentEmail( entity.getStudentEmail() );
        subscriptionResponseDto.studentId( entity.getStudentId() );
        subscriptionResponseDto.studentName( entity.getStudentName() );
        subscriptionResponseDto.studentPhone( entity.getStudentPhone() );
        subscriptionResponseDto.subscriptionMonth( entity.getSubscriptionMonth() );
        subscriptionResponseDto.subscriptionYear( entity.getSubscriptionYear() );
        subscriptionResponseDto.updatedAt( entity.getUpdatedAt() );

        subscriptionResponseDto.remainingSessions( entity.getRemainingSessionCount() );
        subscriptionResponseDto.isOverLimit( entity.isOverLimit() );

        return subscriptionResponseDto.build();
    }
}
