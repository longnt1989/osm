package com.pt.osm.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "data_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dataRequestNumber ;
    private String name;
    private String dataFormat  ;
    private String numberOfUnit  ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedTime ;
    private String deliveryTime ;
    private String note ;
    private String attachment ;
    private boolean confirmation ;
    private long requestId;
    
    @Override
    public String toString() {
    	return name;
    }

}
