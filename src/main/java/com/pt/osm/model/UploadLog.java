package com.pt.osm.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "upload_log")
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UploadLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String label;
    @NotNull
    private String username;
    @Column(name = "created_time")
    private Date createdTime;

    @PrePersist
    public void addCreatedTime() {
        createdTime = new Date();
    }
}
