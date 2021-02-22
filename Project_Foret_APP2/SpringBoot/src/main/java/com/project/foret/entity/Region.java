package com.project.foret.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regionSi;
    private String regionGu;

    @ManyToMany(targetEntity = Member.class, mappedBy = "tags", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Member> members;
    @ManyToMany(targetEntity = Foret.class, mappedBy = "tags", cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Foret> forets;
}