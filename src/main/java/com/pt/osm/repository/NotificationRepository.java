package com.pt.osm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.Notification;


@Repository("notificationRepository")
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
