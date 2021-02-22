package com.project.foret.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "board_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long group_id;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member member;
}
