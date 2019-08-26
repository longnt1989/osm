package com.pt.osm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pt.osm.model.Budget;
import com.pt.osm.model.Comment;
import com.pt.osm.model.DataOffer;
import com.pt.osm.model.DataRequest;
import com.pt.osm.model.GroupChat;
import com.pt.osm.model.MapChat;
import com.pt.osm.model.Offer;
import com.pt.osm.model.Payment;
import com.pt.osm.model.Request;
import com.pt.osm.repository.BudgetRepository;
import com.pt.osm.repository.CommentRepository;
import com.pt.osm.repository.DataOfferRepository;
import com.pt.osm.repository.DataRequestRepository;
import com.pt.osm.repository.GroupChatRepository;
import com.pt.osm.repository.MapChatRepository;
import com.pt.osm.repository.OfferRepository;
import com.pt.osm.repository.PaymentRepository;
import com.pt.osm.repository.RequestRepository;

@Service
public class RequestService {

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	DataRequestRepository dataRequestRepository;

	@Autowired
	BudgetRepository budgetRepository;

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	DataOfferRepository dataOfferRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	GroupChatRepository groupChatRepository;

	@Autowired
	MapChatRepository mapChatRepository;

	public Request saveRequest(Request request) {
		return requestRepository.save(request);
	}

	public List<Request> findAllRequest() {
		return requestRepository.findAll();
	}

	public void deleteRequest(Request request) {
		requestRepository.delete(request);
	}

	public Budget saveBudget(Budget budget) {
		return budgetRepository.save(budget);
	}

	public List<Budget> findAllBudget(Request request) {
		return budgetRepository.findByRequestId(request.getId());
	}

	public void deleteBudget(Budget budget) {
		budgetRepository.delete(budget);
	}

	public DataRequest saveDataRequest(DataRequest dataRequest) {
		return dataRequestRepository.save(dataRequest);
	}

	public List<DataRequest> findAllDataRequest(Request request) {
		return dataRequestRepository.findByRequestId(request.getId());
	}

	public void deleteDataRequest(DataRequest request) {
		dataRequestRepository.delete(request);
	}

	public Comment saveComment(Comment comment) {
		return commentRepository.save(comment);
	}

	public List<Comment> findByLinkId(long requestId) {
		return commentRepository.findByRequestId(requestId);
	}

	public Offer findByRequestId(long requestId) {
		return offerRepository.findByRequestId(requestId);
	}

	public Offer saveOffer(Offer offer) {
		return offerRepository.save(offer);
	}

	public DataOffer saveDataOffer(DataOffer dataOffer) {
		return dataOfferRepository.save(dataOffer);
	}

	public void deleteDataOffer(DataOffer dataOffer) {
		dataOfferRepository.delete(dataOffer);
	}

	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	public List<DataOffer> findAllDataOffer(Request offer) {
		return dataOfferRepository.findByOfferId(offer.getId());
	}

	public List<Payment> findAllPayment(Offer offer) {
		return paymentRepository.findByOfferId(offer.getId());
	}

	@Transactional
	public GroupChat saveGroupChat(GroupChat groupChat) {
		return groupChatRepository.save(groupChat);
	}

	@Transactional
	public GroupChat getGroupChatById(long idGroupChat) {
		return groupChatRepository.getOne(idGroupChat);
	}

	@Transactional
	public List<GroupChat> findGroupByLinkId(long linkId) {
		return groupChatRepository.findByLinkId(linkId);
	}

	@Transactional
	public List<GroupChat> findByTypeAndLinkId(int type, long linkId) {
		return groupChatRepository.findByTypeAndLinkId(type, linkId);
	}

	@Transactional
	public MapChat saveMapChat(MapChat groupChat) {
		return mapChatRepository.save(groupChat);
	}

	@Transactional
	public void deleteMapChat(MapChat groupChat) {
		mapChatRepository.delete(groupChat);
	}

	@Transactional
	public List<MapChat> findByUserId(long userId) {
		return mapChatRepository.findByUserId(userId);
	}

	@Transactional
	public List<MapChat> findByGroupId(long groupId) {
		return mapChatRepository.findByGroupId(groupId);
	}

	@Transactional
	public void deleteGroupChat(GroupChat groupChat) {
		List<MapChat> lst = mapChatRepository.findByGroupId(groupChat.getId());
		for (MapChat mapChat : lst) {
			mapChatRepository.delete(mapChat);
		}
		List<Comment> comments = commentRepository.findByRequestId(groupChat.getId());
		for (Comment mapChat : comments) {
			commentRepository.delete(mapChat);
		}
		groupChatRepository.delete(groupChat);
	}

}