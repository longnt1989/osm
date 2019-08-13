package com.pt.osm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pt.osm.model.Comment;
import com.pt.osm.model.Notification;
import com.pt.osm.repository.NotificationRepository;

@Service("notificationService")

public class NotificationService {
	
	private NotificationRepository notificationRepository;
    
	@Autowired
	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}
	

    public Notification saveNotification(Notification notification) {
       
        return notificationRepository.save(notification);
    }
    
    
	public List<Notification> findAll() {
		return notificationRepository.findAll();
	}

	public void delete(Notification notification) {
		notificationRepository.delete(notification);
	}


	
	
	
	

}
