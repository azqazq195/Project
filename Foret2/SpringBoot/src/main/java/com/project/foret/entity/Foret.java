package com.project.foret.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "foret")
public class Foret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String introduce;
    private int max_member;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;
    @ManyToOne
    @JoinColumn(name = "leader_id")
    private Member leader;

    @ManyToMany(targetEntity = Tag.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tag> tags;
    @ManyToMany(targetEntity = Region.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Region> regions;
    @OneToMany(mappedBy = "foret", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ForetPhoto> photos;
    @ManyToMany(targetEntity = Member.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Member> members;
    @OneToMany(mappedBy = "foret", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Board> boards;

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

    public void addPhoto(ForetPhoto foretPhoto) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(foretPhoto);
        foretPhoto.setForet(this);
    }

    public void addMember(Member member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
    }

    public Foret(Long id) {
        this.id = id;
    }

    public Foret(){

    }
}
