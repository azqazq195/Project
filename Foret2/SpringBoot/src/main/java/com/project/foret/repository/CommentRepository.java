package com.project.foret.repository;

import com.project.foret.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value =
            "SELECT * " +
            "FROM board_comment WHERE board_id = ?1 " +
            "ORDER BY group_id ASC, " +
            "id ASC",
            nativeQuery = true)
    List<Comment> findAllByBoardIdOrderByGroupIdAscIdAsc(Long board_id);
}
