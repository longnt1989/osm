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
@Table(name = "data_offer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataOffer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String dataOfferNumber;
	private String name;
	private String dataFormat;
	private String numberOfUnit;
	private String expectedTime;
	private String submissionDate;
	private String note;
	private String submissionData;
	private String processingTime ;
	private boolean confirmation;
	private long offerId;

	@Override
	public String toString() {
		return name;
	}

}
