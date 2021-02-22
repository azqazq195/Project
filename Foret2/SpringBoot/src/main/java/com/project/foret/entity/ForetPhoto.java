package com.project.foret.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "foret_photo")
public class ForetPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dir;
    private String filename;
    private String originname;
    private int filesize;
    private String filetype;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;

    @ManyToOne
    @JoinColumn(name = "foret_id")
    private Foret foret;
}
