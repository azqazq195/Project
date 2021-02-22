package com.project.foret.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BoardModel {
    private Long id;
    private Long foret_id;
    private int type;
    private int hit;
    private String subject;
    private String content;
    private Date reg_date;
    private Date edit_date;
    private MemberModel member;
    private List<PhotoModel> photos;
    private List<CommentModel> comments;
    private int comment_count;
    private int like_count;
}
