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
    @Column(name = "request_id")
    private Long requestId;
    private String type;
    

    @PrePersist
    public void addCreatedTime() {
        createdTime = new Date();
    }

    public static enum UploadType {

        REQUEST("REQUEST"),ORDER("ORDER");

        private final String value;

        private UploadType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        public static UploadType getEnum(String uploadType) {
            for (UploadType subside : values()) {
                if (subside.value == uploadType) {
                    return subside;
                }
            }
            throw new IllegalArgumentException("No matching constant for [" + uploadType + "]");
        }
    }
    
}
