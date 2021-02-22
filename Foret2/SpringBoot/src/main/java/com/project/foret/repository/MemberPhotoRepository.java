package com.project.foret.repository;

import com.project.foret.entity.MemberPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Long> {
    void deleteByMemberId(Long id);
}
