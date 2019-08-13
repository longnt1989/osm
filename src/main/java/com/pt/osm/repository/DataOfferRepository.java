package com.pt.osm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.osm.model.DataOffer;

@Repository()
public interface DataOfferRepository extends JpaRepository<DataOffer, Long> {
	List<DataOffer> findByOfferId(long offerId);
}
