package com.kobi.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
