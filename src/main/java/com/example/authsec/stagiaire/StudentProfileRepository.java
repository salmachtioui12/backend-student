package com.example.authsec.stagiaire;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Integer> {
    Optional<StudentProfile> findByUserId(Integer userId);
    Optional<StudentProfile> findByUserEmail(String email);


    }

