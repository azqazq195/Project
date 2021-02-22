package com.project.foret.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int type;
    private int hit;
    private String subject;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;
    private Date edit_date;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "foret_id")
    private Foret foret;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoardPhoto> photos;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
    @ManyToMany(targetEntity = Member.class, mappedBy = "likes", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Member> members;

    public void addPhoto(BoardPhoto boardPhoto) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(boardPhoto);
        boardPhoto.setBoard(this);
    }

    public Board(Long id) {
        this.id = id;
    }

    public Board() {
    }

}
