package com.pt.osm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "budget")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String budgetNumber;
    private String name;
    private String dataFormat;
    private String unitPrice;
    private String note ;
    private String attachment ;
    private boolean confirmation ;
    private long requestId;
    
    @Override
    public String toString() {
    	return name;
    }

}
