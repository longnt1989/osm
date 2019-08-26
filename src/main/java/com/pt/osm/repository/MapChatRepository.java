package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.MapChat;

@Repository("mapChatRepository")
public interface MapChatRepository extends JpaRepository<MapChat, Long> {
	List<MapChat> findByUserId(long userId);
	List<MapChat> findByGroupId(long groupId);
}
