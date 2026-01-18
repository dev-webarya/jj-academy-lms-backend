package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.InstructorRequestDto;
import com.artclassmanagement.dto.response.InstructorResponseDto;
import com.artclassmanagement.entity.Instructor;
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
public class InstructorMapperImpl implements InstructorMapper {

    @Override
    public Instructor toEntity(InstructorRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Instructor.InstructorBuilder instructor = Instructor.builder();

        instructor.bio( dto.getBio() );
        instructor.displayName( dto.getDisplayName() );
        instructor.linkedInUrl( dto.getLinkedInUrl() );
        instructor.portfolioUrl( dto.getPortfolioUrl() );
        instructor.profileImageUrl( dto.getProfileImageUrl() );
        List<String> list = dto.getQualifications();
        if ( list != null ) {
            instructor.qualifications( new ArrayList<String>( list ) );
        }
        List<String> list1 = dto.getSpecializations();
        if ( list1 != null ) {
            instructor.specializations( new ArrayList<String>( list1 ) );
        }
        instructor.userId( dto.getUserId() );
        instructor.yearsOfExperience( dto.getYearsOfExperience() );

        return instructor.build();
    }

    @Override
    public InstructorResponseDto toDto(Instructor entity) {
        if ( entity == null ) {
            return null;
        }

        InstructorResponseDto instructorResponseDto = new InstructorResponseDto();

        instructorResponseDto.setBio( entity.getBio() );
        instructorResponseDto.setCreatedAt( entity.getCreatedAt() );
        instructorResponseDto.setDisplayName( entity.getDisplayName() );
        instructorResponseDto.setEmail( entity.getEmail() );
        instructorResponseDto.setId( entity.getId() );
        instructorResponseDto.setIsActive( entity.getIsActive() );
        instructorResponseDto.setLinkedInUrl( entity.getLinkedInUrl() );
        instructorResponseDto.setPhoneNumber( entity.getPhoneNumber() );
        instructorResponseDto.setPortfolioUrl( entity.getPortfolioUrl() );
        instructorResponseDto.setProfileImageUrl( entity.getProfileImageUrl() );
        List<String> list = entity.getQualifications();
        if ( list != null ) {
            instructorResponseDto.setQualifications( new ArrayList<String>( list ) );
        }
        List<String> list1 = entity.getSpecializations();
        if ( list1 != null ) {
            instructorResponseDto.setSpecializations( new ArrayList<String>( list1 ) );
        }
        instructorResponseDto.setUpdatedAt( entity.getUpdatedAt() );
        instructorResponseDto.setUserId( entity.getUserId() );
        instructorResponseDto.setYearsOfExperience( entity.getYearsOfExperience() );

        return instructorResponseDto;
    }

    @Override
    public void updateEntity(InstructorRequestDto dto, Instructor entity) {
        if ( dto == null ) {
            return;
        }

        entity.setBio( dto.getBio() );
        entity.setDisplayName( dto.getDisplayName() );
        entity.setLinkedInUrl( dto.getLinkedInUrl() );
        entity.setPortfolioUrl( dto.getPortfolioUrl() );
        entity.setProfileImageUrl( dto.getProfileImageUrl() );
        if ( entity.getQualifications() != null ) {
            List<String> list = dto.getQualifications();
            if ( list != null ) {
                entity.getQualifications().clear();
                entity.getQualifications().addAll( list );
            }
            else {
                entity.setQualifications( null );
            }
        }
        else {
            List<String> list = dto.getQualifications();
            if ( list != null ) {
                entity.setQualifications( new ArrayList<String>( list ) );
            }
        }
        if ( entity.getSpecializations() != null ) {
            List<String> list1 = dto.getSpecializations();
            if ( list1 != null ) {
                entity.getSpecializations().clear();
                entity.getSpecializations().addAll( list1 );
            }
            else {
                entity.setSpecializations( null );
            }
        }
        else {
            List<String> list1 = dto.getSpecializations();
            if ( list1 != null ) {
                entity.setSpecializations( new ArrayList<String>( list1 ) );
            }
        }
        entity.setYearsOfExperience( dto.getYearsOfExperience() );
    }
}
