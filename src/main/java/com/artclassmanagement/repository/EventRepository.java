package com.artclassmanagement.repository;

import com.artclassmanagement.entity.Event;
import com.artclassmanagement.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Page<Event> findByIsDeletedFalse(Pageable pageable);

    Page<Event> findByIsPublicTrueAndIsDeletedFalse(Pageable pageable);

    Page<Event> findByEventTypeAndIsDeletedFalse(EventType eventType, Pageable pageable);

    List<Event> findByStartDateTimeAfterAndIsDeletedFalse(Instant after);

    List<Event> findByStartDateTimeBetweenAndIsDeletedFalse(Instant start, Instant end);
}
