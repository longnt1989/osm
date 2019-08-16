package com.pt.osm.repository;

import com.pt.osm.model.UploadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadLogRepository extends JpaRepository<UploadLog, Long> {
    List<UploadLog> findByUsername(String username);
    UploadLog findByName(String name);

    @Query(value = "SELECT t FROM UploadLog t WHERE (t.label LIKE :label OR :label IS NULL) " +
            " AND (t.username = :username OR :username IS NULL)" +
            " AND (t.requestId = :requestId OR :requestId IS NULL)")
    List<UploadLog> searchRequest(@Param("label") String label,
                                  @Param("username") String username,
                                  @Param("requestId") Long requestId);
}