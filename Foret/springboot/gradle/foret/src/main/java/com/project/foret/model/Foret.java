package com.project.foret.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class Foret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long leader_id;
    private String name;
    private String introduce;
    private int max_member;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "foret_tag", joinColumns = @JoinColumn(name = "foret_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "foret_region", joinColumns = @JoinColumn(name = "foret_id"), inverseJoinColumns = @JoinColumn(name = "region_id"))
    private List<Region> regions;

    @OneToMany(mappedBy = "foret", cascade = CascadeType.ALL)
    private List<ForetPhoto> photos;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "foret_member", joinColumns = @JoinColumn(name = "foret_id"), inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> members;

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        tag.addForet(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.removeForet(this);
    }

    public void addRegion(Region region) {
        if (regions == null) {
            regions = new ArrayList<>();
        }
        regions.add(region);
        region.addForet(this);
    }

    public void removeRegion(Region region) {
        regions.remove(region);
        region.removeForet(this);
    }

    public void addPhoto(ForetPhoto foretPhoto) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(foretPhoto);
        foretPhoto.setForet(this);
    }

    public void removePhoto(ForetPhoto foretPhoto) {
        photos.remove(foretPhoto);
        foretPhoto.setForet(null);
    }

    public void addMember(Member member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
        member.addForet(this);
    }

    public void removeMember(Member member) {
        members.remove(member);
        member.removeForet(this);
    }

}
