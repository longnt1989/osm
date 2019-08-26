package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.GroupChat;

@Repository("groupChatRepository")
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
	List<GroupChat> findByLinkId(long linkId);
	
	List<GroupChat> findByTypeAndLinkId(int type,long linkId);
}
