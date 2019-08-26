package com.pt.osm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "map_chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MapChat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private long userId;
	private long groupId;
}
