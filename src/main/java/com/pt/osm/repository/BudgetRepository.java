package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.Budget;

@Repository()
public interface BudgetRepository extends JpaRepository<Budget, Long> {
	List<Budget> findByRequestId(long requestId);
}
