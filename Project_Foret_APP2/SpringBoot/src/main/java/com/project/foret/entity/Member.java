package com.project.foret.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private String deviceToken;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;

    @ManyToMany(targetEntity = Tag.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tag> tags;
    @ManyToMany(targetEntity = Region.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Region> regions;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberPhoto> photos;
    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Foret> leadForets;
    @ManyToMany(targetEntity = Foret.class, mappedBy = "members", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Foret> forets;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Board> boards;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
    @ManyToMany(targetEntity = Board.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Board> likes;

    public void addLike(Board board) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(board);
    }

    public void removeLike(Board board) {
        likes.remove(board);
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    public void addRegion(Region region) {
        if (regions == null) {
            regions = new ArrayList<>();
        }
        regions.add(region);
    }

    public void addPhoto(MemberPhoto memberPhoto) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(memberPhoto);
        memberPhoto.setMember(this);
    }

    public void addForet(Foret foret) {
        if (forets == null) {
            forets = new ArrayList<>();
        }
        forets.add(foret);
    }

    public Member(Long id) {
        this.id = id;
    }

    public Member() {
    }

}
