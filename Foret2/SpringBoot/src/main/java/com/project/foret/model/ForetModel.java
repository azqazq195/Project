package com.project.foret.model;

import com.project.foret.entity.Member;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ForetModel {
    private Long id;
    private MemberModel leader;
    private String name;
    private String introduce;
    private int max_member;
    private int current_member;
    private Date reg_date;
    private List<TagModel> tags;
    private List<RegionModel> regions;
    private List<PhotoModel> photos;
    private List<MemberModel> members;
    private List<BoardModel> boards;
}
