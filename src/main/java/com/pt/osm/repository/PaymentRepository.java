package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.Payment;

@Repository()
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByOfferId(long offerId);
}
