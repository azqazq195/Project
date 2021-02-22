package com.project.foret.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    // 멤버
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Member> members;

    public void addMember(Member member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    // 포레
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Foret> forets;

    public void addForet(Foret foret) {
        if (forets == null) {
            forets = new ArrayList<>();
        }
        forets.add(foret);
    }

    public void removeForet(Foret foret) {
        forets.remove(foret);
    }

}
