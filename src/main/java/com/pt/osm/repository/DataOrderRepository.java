package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.DataOrder;

@Repository()
public interface DataOrderRepository extends JpaRepository<DataOrder, Long> {
	List<DataOrder> findByOrderId(long orderId);
}
