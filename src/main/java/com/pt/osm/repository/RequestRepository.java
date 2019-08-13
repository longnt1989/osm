package com.pt.osm.repository;

import com.pt.osm.model.Request;
import com.pt.osm.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface RequestRepository extends JpaRepository<Request, Long> {

}
