package com.pt.osm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.Offer;

@Repository()
public interface OfferRepository extends JpaRepository<Offer, Long> {
	Offer findByRequestId(long requestId);
}
