package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.Comment;

@Repository("commentRepository")
public interface CommentRepository extends JpaRepository<Comment, Long> {
	 List<Comment> findByRequestId(long requestId);
}
