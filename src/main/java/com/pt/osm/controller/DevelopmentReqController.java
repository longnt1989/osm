package com.pt.osm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	private Panel developmentReqDiv;
	@Wire
	private Label lbRqNumber, lbCreateDate;
	@Wire
	private Textbox txtReqDescription;
	@Wire
	private Rows rowsOrderDev, rowsBudget;
	@Wire
	private A aAddBudget, aAddOrder;

	private Request request;
	private List<DataRequest> dataRequests;
	private List<Budget> budgets;

	@Autowired
	private RequestService requestService;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		developmentReqDiv.setAttribute("DevelopmentReqController", this);
		txtReqDescription.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				request.setDescription(txtReqDescription.getValue());
				requestService.saveRequest(request);
			}
		});

		aAddBudget.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				budgets = requestService.findAllBudget(request);
				Budget budget = new Budget();
				budget.setBudgetNumber(request.getRequestNumber() + "-" + "00" + (budgets.size() + 1));
				budget.setRequestId(request.getId());
				budget.setConfirmation(false);
				budget = requestService.saveBudget(budget);
				addRowBudget(budget);
			};
		});

		aAddOrder.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				dataRequests = requestService.findAllDataRequest(request);
				DataRequest rq = new DataRequest();
				rq.setDataRequestNumber(request.getRequestNumber() + "-" + "00" + (dataRequests.size() + 1));
				rq.setRequestId(request.getId());
				rq.setConfirmation(false);
				rq = requestService.saveDataRequest(rq);
				addRowOrder(rq);
			};
		});

	}

	public void setRequest(Request request) {
		this.request = request;
		txtReqDescription.setValue(request.getDescription());
		lbRqNumber.setValue(request.getRequestNumber());
		lbCreateDate.setValue(new SimpleDateFormat("dd/MM/yyyy").format(request.getCreateDate()));
		dataRequests = requestService.findAllDataRequest(request);
		budgets = requestService.findAllBudget(request);
		for (DataRequest rq : dataRequests) {
			addRowOrder(rq);
		}
		for (Budget rq : budgets) {
			addRowBudget(rq);
		}
	}

	private void addRowOrder(DataRequest dataRequest) {
		Row row = new Row();
		row.setParent(rowsOrderDev);
		Label txtNumber = new Label(dataRequest.getDataRequestNumber());
		txtNumber.setStyle("width:100%");
		txtNumber.setParent(row);
		DivChat divChatG = new DivChat(dataRequest.getId(), DivChat.View_DataRequest, 0);
		divChatG.setParent(row);
		addColumnData(dataRequest, row, "name");
		addColumnData(dataRequest, row, "dataFormat");
		addColumnData(dataRequest, row, "numberOfUnit");
		addColumnData(dataRequest, row, "deliveryTime");
		addColumnData(dataRequest, row, "note");
		addColumnData(dataRequest, row, "attachment");
		Checkbox chb = new Checkbox("Confirm");
		chb.setWidth("100%");
		chb.setParent(row);
	}

	private void addRowBudget(Budget budget) {
		Row row = new Row();
		row.setParent(rowsBudget);
		Label txtNumber = new Label(budget.getBudgetNumber());
		txtNumber.setStyle("width:100%");
		txtNumber.setParent(row);
		DivChat divChatG = new DivChat(budget.getId(), DivChat.View_Budget, 0);
		divChatG.setParent(row);
		addColumnBudget(budget, row, "name");
		addColumnBudget(budget, row, "unitPrice ");
		addColumnBudget(budget, row, "note");
		addColumnBudget(budget, row, "attachment");
		Checkbox chb = new Checkbox("Confirm");
		chb.setWidth("100%");
		chb.setParent(row);
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
		txtField.setRows(10);
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

	private void addColumnBudget(Budget budget, Row row, String field) {
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
					requestService.saveBudget(budget);
					break;
				case "unitPrice ":
					budget.setUnitPrice(txtField.getValue());
					requestService.saveBudget(budget);
					break;
				case "note":
					budget.setNote(txtField.getValue());
					requestService.saveBudget(budget);
					break;
				case "attachment":
					budget.setAttachment(txtField.getValue());
					requestService.saveBudget(budget);
					break;

				default:
					break;
				}

			}
		});
	}

}
