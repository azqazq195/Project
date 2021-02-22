package com.project.foret.repository;

import com.project.foret.entity.Region;
import com.project.foret.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByRegionSiAndRegionGu(String regionSi, String regionGu);
}
