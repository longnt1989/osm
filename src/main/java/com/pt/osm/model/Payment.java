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
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String paymentNumber ;
    private String name;
    private String unitPrice;
    private String note ;
    private String attachment ;
    private boolean confirmation ;
    private long offerId;
    
    @Override
    public String toString() {
    	return name;
    }

}
