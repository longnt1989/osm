package com.pt.osm.repository;

import com.pt.osm.model.UploadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadLogRepository extends JpaRepository<UploadLog, Long> {
    List<UploadLog> findByUsername(String username);
}