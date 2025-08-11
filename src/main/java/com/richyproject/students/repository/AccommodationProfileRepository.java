package com.richyproject.students.repository;

import com.richyproject.students.model.AccommodationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccommodationProfileRepository extends JpaRepository<AccommodationProfile,Integer> {

        Optional<AccommodationProfile> findByStudentUsername(String username);






}
