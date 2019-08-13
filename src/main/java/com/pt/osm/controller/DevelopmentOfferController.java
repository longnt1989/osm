package com.pt.osm.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.pt.osm.OsmApplication;
import com.pt.osm.component.DivChat;
import com.pt.osm.model.Budget;
import com.pt.osm.model.DataOffer;
import com.pt.osm.model.DataRequest;
import com.pt.osm.model.Offer;
import com.pt.osm.model.Payment;
import com.pt.osm.model.Request;
import com.pt.osm.model.User;
import com.pt.osm.service.RequestService;

public class DevelopmentOfferController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Panel developmentOfferDiv;
	@Wire
	private Label lbOfNumber, lbOfCreateDate;
	@Wire
	private Textbox txtOfDescription;
	@Wire
	private Rows rowsOfferData, rowsOfferPayment;
	@Wire
	private A aAddOfferData, aAddOfferPayment;

	private Offer offer;
	private List<DataOffer> dataOffers;
	private List<Payment> payments;

	@Autowired
	private RequestService requestService;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		developmentOfferDiv.setAttribute("DevelopmentOfferController", this);
		txtOfDescription.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				offer.setDescription(txtOfDescription.getValue());
				requestService.saveOffer(offer);
			}
		});

		aAddOfferPayment.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				payments = requestService.findAllPayment(offer);
				Payment budget = new Payment();
				budget.setPaymentNumber(offer.getOfferNumber() + "-" + "00" + (payments.size() + 1));
				budget.setOfferId(offer.getId());
				budget.setConfirmation(false);
				budget = requestService.savePayment(budget);
				addRowBudget(budget);
			};
		});

		aAddOfferData.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				dataOffers = requestService.findAllDataOffer(offer);
				DataOffer rq = new DataOffer();
				rq.setDataOfferNumber(offer.getOfferNumber() + "-" + "00" + (dataOffers.size() + 1));
				rq.setOfferId(offer.getId());
				rq.setConfirmation(false);
				rq = requestService.saveDataOffer(rq);
				addRowOrder(rq);
			};
		});

	}

	public void setOffer(Offer offer) {
		this.offer = offer;
		txtOfDescription.setValue(offer.getDescription());
		lbOfNumber.setValue(offer.getOfferNumber());
		lbOfCreateDate.setValue(new SimpleDateFormat("dd/MM/yyyy").format(offer.getCreateDate()));
		dataOffers = requestService.findAllDataOffer(offer);
		payments = requestService.findAllPayment(offer);
		for (DataOffer rq : dataOffers) {
			addRowOrder(rq);
		}
		for (Payment rq : payments) {
			addRowBudget(rq);
		}
	}

	private void addRowOrder(DataOffer dataRequest) {
		Row row = new Row();
		row.setParent(rowsOfferData);
		Label txtNumber = new Label(dataRequest.getDataOfferNumber());
		txtNumber.setStyle("width:100%");
		txtNumber.setParent(row);
		DivChat divChatG = new DivChat(dataRequest.getId(), DivChat.View_DataRequest, 0);
		divChatG.setParent(row);
		addColumnData(dataRequest, row, "name");
		addColumnData(dataRequest, row, "deadline");
		addColumnData(dataRequest, row, "dataFormat");
		addColumnData(dataRequest, row, "numberOfUnit");
		addColumnData(dataRequest, row, "deliveryTime");
		addColumnData(dataRequest, row, "note");
		addColumnData(dataRequest, row, "submissionDate");
		addColumnData(dataRequest, row, "submissionData");
		addColumnData(dataRequest, row, "processingTime");
		Checkbox chb = new Checkbox("Confirm");
		chb.setWidth("100%");
		chb.setParent(row);
	}

	private void addRowBudget(Payment budget) {
		Row row = new Row();
		row.setParent(rowsOfferPayment);
		Label txtNumber = new Label(budget.getPaymentNumber());
		txtNumber.setStyle("width:100%");
		txtNumber.setParent(row);
		DivChat divChatG = new DivChat(budget.getId(), DivChat.View_Budget, 0);
		divChatG.setParent(row);
		addColumnBudget(budget, row, "name");
		addColumnBudget(budget, row, "unitPrice ");
		addColumnBudget(budget, row, "attachment ");
		addColumnBudget(budget, row, "note");

		Checkbox chb = new Checkbox("Confirm");
		chb.setWidth("100%");
		chb.setParent(row);
	}

	private void addColumnData(DataOffer dataRequest, Row row, String field) {
		Textbox txtField = new Textbox();
		txtField.setInplace(true);
		switch (field) {
		case "name":
			txtField.setValue(dataRequest.getName());
			break;
		case "dataFormat":
			txtField.setValue(dataRequest.getDataFormat());
			break;
		case "numberOfUnit":
			txtField.setValue(dataRequest.getNumberOfUnit());
			break;
		case "deliveryTime":
			txtField.setValue(dataRequest.getExpectedTime());
			break;
		case "note":
			txtField.setValue(dataRequest.getNote());
			break;
		case "submissionDate":
			txtField.setValue(dataRequest.getSubmissionDate());
			break;
		case "submissionData":
			txtField.setValue(dataRequest.getSubmissionData());
			break;
		case "processingTime":
			txtField.setValue(dataRequest.getProcessingTime());
			break;
		default:
			break;
		}
		txtField.setWidth("100%");
		txtField.setParent(row);
		txtField.setRows(10);
		txtField.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				switch (field) {
				case "name":
					dataRequest.setName(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "dataFormat":
					dataRequest.setDataFormat(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "numberOfUnit":
					dataRequest.setNumberOfUnit(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "deliveryTime":
					dataRequest.setExpectedTime(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "note":
					dataRequest.setNote(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "submissionData":
					dataRequest.setSubmissionData(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "submissionDate":
					dataRequest.setSubmissionDate(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;
				case "processingTime":
					dataRequest.setProcessingTime(txtField.getValue());
					requestService.saveDataOffer(dataRequest);
					break;

				default:
					break;
				}

			}
		});
	}

	private void addColumnBudget(Payment budget, Row row, String field) {
		Textbox txtField = new Textbox();
		txtField.setInplace(true);
		switch (field) {
		case "name":
			txtField.setValue(budget.getName());
			break;
		case "unitPrice ":
			txtField.setValue(budget.getUnitPrice());
			break;
		case "note":
			txtField.setValue(budget.getNote());
			break;
		case "attachment":
			txtField.setValue(budget.getAttachment());
			break;

		default:
			break;
		}
		txtField.setWidth("100%");
		txtField.setParent(row);
		txtField.setRows(10);
		txtField.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				switch (field) {
				case "name":
					budget.setName(txtField.getValue());
					requestService.savePayment(budget);
					break;
				case "unitPrice ":
					budget.setUnitPrice(txtField.getValue());
					requestService.savePayment(budget);
					break;
				case "note":
					budget.setNote(txtField.getValue());
					requestService.savePayment(budget);
					break;
				case "attachment":
					budget.setAttachment(txtField.getValue());
					requestService.savePayment(budget);
					break;

				default:
					break;
				}

			}
		});
	}

}
