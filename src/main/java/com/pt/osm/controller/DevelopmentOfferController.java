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
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
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
	private Rows rowsOfferData;
	@Wire
	private A aAddOfferData;

	private Request offer;
	private List<DataOffer> dataOffers;

	@Autowired
	private RequestService requestService;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		developmentOfferDiv.setAttribute("DevelopmentOfferController", this);

		aAddOfferData.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				dataOffers = requestService.findAllDataOffer(offer);
				DataOffer rq = new DataOffer();
				rq.setDataOfferNumber("00" + (dataOffers.size() + 1));
				rq.setOfferId(offer.getId());
				rq.setConfirmation(false);
				rq = requestService.saveDataOffer(rq);
				System.out.println(rq);
				addRowOrder(rq);
			};
		});

	}

	public void setRequest(Request offer) {
		this.offer = offer;

		dataOffers = requestService.findAllDataOffer(offer);
		System.out.println(dataOffers);
		for (DataOffer rq : dataOffers) {
			addRowOrder(rq);
		}

	}

	private void addRowOrder(DataOffer dataRequest) {
		Row row = new Row();
		row.setParent(rowsOfferData);
		try {
			Label txtNumber = new Label(dataRequest.getDataOfferNumber());
			txtNumber.setStyle("width:100%");
			txtNumber.setParent(row);
			addColumnData(dataRequest, row, "name");
			addColumnData(dataRequest, row, "deadline");
			addColumnData(dataRequest, row, "dataFormat");
			addColumnData(dataRequest, row, "numberOfUnit");
			addColumnData(dataRequest, row, "deliveryTime");
			addColumnData(dataRequest, row, "note");
			addColumnData(dataRequest, row, "submissionDate");
			addColumnData(dataRequest, row, "processingTime");
			Div divIcon = new Div();
			divIcon.setParent(row);

			A aDelete = new A("");
			Image imgDelete = new Image("/img/iconRemove.png");
			imgDelete.setParent(aDelete);
			imgDelete.setStyle("height: 20px;margin-right:10px");
			aDelete.setParent(divIcon);
			aDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					requestService.deleteDataOffer(dataRequest);
					rowsOfferData.removeChild(row);
				}
			});
			Image img = new Image("/img/iconLoad.gif");
			img.setStyle("height: 20px;");
			img.setParent(divIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addColumnData(DataOffer dataRequest, Row row, String field) {
		Textbox txtField = new Textbox();
		txtField.setInplace(true);
		txtField.setHeight("25px");
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtField.setWidth("100%");
		txtField.setParent(row);
		txtField.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				try {
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
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

}
