package com.project.foret.model;

import lombok.Data;

import java.util.Date;

@Data
public class PhotoModel {
    private Long id;
    private String dir;
    private String filename;
    private String originname;
    private int filesize;
    private String filetype;
    private Date reg_date;
}
