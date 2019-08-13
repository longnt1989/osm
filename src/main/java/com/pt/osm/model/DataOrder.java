package com.pt.osm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "data_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dataOrderNumber ;
    private String name;
    private String dataFormat  ;
    private String numberOfUnit  ;
    private String submissions ;
    private String note ;
    private String notice ;
    private String sample ;
    private String attachment ;
    private boolean confirmation ;
    private long orderId;
    
    @Override
    public String toString() {
    	return name;
    }

}
