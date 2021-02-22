package com.project.foret.repository;

import com.project.foret.entity.Foret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForetRepository extends JpaRepository<Foret, Long> {
    List<Foret> findByMembersId(Long id);

    @Query("Select f from Foret f where f.name like %:name%")
    List<Foret> findByContainingName(String name);

    Optional<Foret> findByIdAndMembersId(Long foret_id, Long member_id);
}
