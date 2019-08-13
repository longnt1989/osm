package com.pt.osm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "comment_osm")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int type; // 0=German, 1=Vietnamese
    private String name;
    private String content;
    private String typeView;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    private long userId;
    private long requestId;
    
    @PrePersist
    void preInsert() {
       if (this.typeView == null)
           this.typeView = "Request";
    }

}
