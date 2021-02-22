package com.project.foret.repository;

import com.project.foret.entity.ForetPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForetPhotoRepository extends JpaRepository<ForetPhoto, Long> {
    void deleteByForetId(Long id);
}
