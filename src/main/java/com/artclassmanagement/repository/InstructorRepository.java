package com.artclassmanagement.repository;

import com.artclassmanagement.entity.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends MongoRepository<Instructor, String> {
    Optional<Instructor> findByUserId(String userId);

    Optional<Instructor> findByUserIdAndIsDeletedFalse(String userId);

    Page<Instructor> findByIsDeletedFalse(Pageable pageable);

    Page<Instructor> findByIsActiveAndIsDeletedFalse(Boolean isActive, Pageable pageable);

    boolean existsByUserId(String userId);
}
