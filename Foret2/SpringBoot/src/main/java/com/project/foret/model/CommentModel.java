package com.project.foret.model;

import lombok.Data;

import java.util.Date;

@Data
public class CommentModel {
    private Long id;
    private Long group_id;
    private String content;
    private Date reg_date;
    private BoardModel board;
    private MemberModel member;
}
