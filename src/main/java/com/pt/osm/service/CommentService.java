package com.pt.osm.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pt.osm.model.Comment;
import com.pt.osm.model.Request;
import com.pt.osm.repository.CommentRepository;

@Service("commentService")

public class CommentService {
	
	
	
    private CommentRepository commentRepository;
    @Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

   

    
    public Comment saveComment(Comment comment) {
       
        return commentRepository.save(comment);
    }
    
    
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}


}
