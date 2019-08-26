package com.pt.osm.controller;

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
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.pt.osm.OsmApplication;
import com.pt.osm.model.DataRequest;
import com.pt.osm.model.Request;
import com.pt.osm.model.User;
import com.pt.osm.service.RequestService;

public class DevelopmentReqController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Panel developmentOrderDiv;
	@Wire
	private Rows rowsOrderDev;
	@Wire
	private A aDataRequest;

	private Request request;
	private List<DataRequest> dataRequests;

	@Autowired
	private RequestService requestService;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		developmentOrderDiv.setAttribute("DevelopmentReqController", this);
		aDataRequest.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				dataRequests = requestService.findAllDataRequest(request);
				DataRequest rq = new DataRequest();
				rq.setDataRequestNumber("00" + (dataRequests.size() + 1));
				rq.setRequestId(request.getId());
				rq.setConfirmation(false);
				rq = requestService.saveDataRequest(rq);
				addRowOrder(rq);
			};
		});

	}

	public void setRequest(Request request) {
		this.request = request;
		dataRequests = requestService.findAllDataRequest(request);
		for (DataRequest rq : dataRequests) {
			addRowOrder(rq);
		}
	}

	private void addRowOrder(DataRequest dataRequest) {
		Row row = new Row();
		row.setParent(rowsOrderDev);
		Label txtNumber = new Label(dataRequest.getDataRequestNumber());
		txtNumber.setStyle("width:100%");
		txtNumber.setParent(row);
		addColumnData(dataRequest, row, "name");
		addColumnData(dataRequest, row, "dataFormat");
		addColumnData(dataRequest, row, "numberOfUnit");
		addColumnData(dataRequest, row, "amount");
		addColumnData(dataRequest, row, "deliveryTime");
		addColumnData(dataRequest, row, "note");
		addColumnData(dataRequest, row, "attachment");
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
				requestService.deleteDataRequest(dataRequest);
				rowsOrderDev.removeChild(row);
			}
		});
		
		Image img = new Image("/img/iconLoad.gif");
		img.setStyle("height: 20px;");
		img.setParent(divIcon);
	}

	private void addColumnData(DataRequest dataRequest, Row row, String field) {
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
			txtField.setValue(dataRequest.getDeliveryTime());
			break;
		case "note":
			txtField.setValue(dataRequest.getNote());
			break;
		case "attachment":
			txtField.setValue(dataRequest.getAttachment());
			break;

		default:
			break;
		}
		txtField.setWidth("100%");
		txtField.setParent(row);
		txtField.setHeight("25px");
		txtField.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				switch (field) {
				case "name":
					dataRequest.setName(txtField.getValue());
					requestService.saveDataRequest(dataRequest);
					break;
				case "dataFormat":
					dataRequest.setDataFormat(txtField.getValue());
					requestService.saveDataRequest(dataRequest);
					break;
				case "numberOfUnit":
					dataRequest.setNumberOfUnit(txtField.getValue());
					requestService.saveDataRequest(dataRequest);
					break;
				case "deliveryTime":
					dataRequest.setDeliveryTime(txtField.getValue());
					requestService.saveDataRequest(dataRequest);
					break;
				case "note":
					dataRequest.setNote(txtField.getValue());
					requestService.saveDataRequest(dataRequest);
					break;
				case "attachment":
					dataRequest.setAttachment(txtField.getValue());
					requestService.saveDataRequest(dataRequest);
					break;

				default:
					break;
				}

			}
		});
	}

}
