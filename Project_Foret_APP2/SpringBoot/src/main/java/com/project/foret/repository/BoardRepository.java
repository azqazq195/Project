package com.project.foret.repository;

import com.project.foret.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value =
            "(SELECT * " +
            "FROM board WHERE foret_id = ?1 AND TYPE = 1 " +
            "ORDER BY id DESC " +
            "LIMIT 5) " +
            "UNION ALL " +
            "(SELECT * " +
            "FROM board WHERE foret_id = ?1 AND TYPE = 3 " +
            "ORDER BY id DESC " +
            "LIMIT 5 )",
    nativeQuery = true)
    List<Board> findTop5ByForetIdOrderByIdDesc(Long foret_id);

    @Query(value =
            "SELECT  " +
            "bb.id, " +
            "bb.`type`, " +
            "bb.hit, " +
            "bb.subject, " +
            "bb.content, " +
            "bb.reg_date, " +
            "bb.edit_date, " +
            "bb.foret_id, " +
            "bb.writer_id, " +
            "cc.comment_count " +
            "FROM board as bb " +
            "LEFT JOIN ( " +
            "SELECT *, COUNT(*) AS comment_count " +
            "FROM  board_comment " +
            "GROUP BY board_id) as cc " +
            "ON bb.id = cc.board_id " +
            "WHERE TYPE = ?1 " +
            "ORDER BY cc.comment_count DESC, " +
            "bb.id DESC", nativeQuery = true)
    List<Board> findByTypeOrderByCommentCount(int type);

    List<Board> findByForetIdAndTypeOrderByIdDesc(Long foret_id, int type);

    List<Board> findByTypeOrderByIdDesc(int type);

}
