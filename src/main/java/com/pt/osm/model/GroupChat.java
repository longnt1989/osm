package com.pt.osm.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int type; // 0 - non task, 1 - task
	private String name;
	private Date time;
	private long linkId;
	@Override
	public String toString() {
		return  name;
	}
	
	
}
