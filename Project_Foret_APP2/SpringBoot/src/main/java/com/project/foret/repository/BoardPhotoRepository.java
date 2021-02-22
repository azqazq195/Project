package com.project.foret.repository;

import com.project.foret.entity.BoardPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPhotoRepository extends JpaRepository<BoardPhoto, Long> {
    void deleteByBoardId(Long id);
}
