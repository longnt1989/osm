package com.pt.osm.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.pt.osm.OsmApplication;
import com.pt.osm.component.DivChat;
import com.pt.osm.model.Request;
import com.pt.osm.model.User;
import com.pt.osm.service.RequestService;
import com.pt.osm.service.UserService;

public class GeneralController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Rows rowsGeneral;
	@Wire
	private Panel panelGeneral;

	@Autowired
	private RequestService requestService;
	private List<Request> requests;
	private HomeController homeController;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		panelGeneral.setAttribute("GeneralController", this);
		loadAll();
	}

	private void loadAll() {
		requests = requestService.findAllRequest();
		int index = 1;
		for (Request rq : requests) {
			createRow(rq, index);
			index++;
		}
	}

	private void createRow(Request rq, int index) {
		String code = rq.getRequestNumber();
		Row row = new Row();
		row.setParent(rowsGeneral);

		Label txtNr = new Label(String.valueOf(index));
		txtNr.setStyle("width:100%");
		txtNr.setParent(row);

		A txtNumber = new A(code);
		txtNumber.setStyle("width:100%");
		txtNumber.setParent(row);
		txtNumber.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				homeController.showRequestDetail(rq);

			}
		});
		A order1 = new A("001-001");
		order1.setParent(row);

		Textbox txtName = new Textbox(rq.getName());
		txtName.setWidth("100%");
		txtName.setHeight("25px");
		txtName.setParent(row);
		txtName.setInplace(true);
		txtName.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				rq.setName(txtName.getValue());
				requestService.saveRequest(rq);
			}
		});

		Textbox txtProgram = new Textbox("");
		txtProgram.setWidth("100%");
		txtProgram.setHeight("25px");
		txtProgram.setParent(row);
		txtProgram.setInplace(true);
		Textbox txtOwrer = new Textbox("");
		txtOwrer.setWidth("100%");
		txtOwrer.setHeight("25px");
		txtOwrer.setParent(row);
		txtOwrer.setInplace(true);

		A aDetail = new A("");
		Image imgDetail = new Image("/img/iconForder.jfif");
		imgDetail.setParent(aDetail);
		imgDetail.setStyle("height: 20px;");
		aDetail.setParent(row);

		Datebox deadline = new Datebox();
		deadline.setWidth("100%");
		deadline.setHeight("25px");
		deadline.setParent(row);
		deadline.setFormat("dd/MM/yyyy");
		deadline.setInplace(true);

		A aOdder = new A("");
		Image imgOdder = new Image("/img/iconLoad.gif");
		imgOdder.setParent(aOdder);
		imgOdder.setStyle("height: 20px;");
		aOdder.setParent(row);

		A aDelete = new A("");
		Image imgDelete = new Image("/img/iconRemove.png");
		imgDelete.setParent(aDelete);
		imgDelete.setStyle("height: 20px;");
		aDelete.setParent(row);
		aDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				requestService.deleteRequest(rq);
				rowsGeneral.removeChild(row);
			}
		});
	}

	public void createRequest() {
		requests = requestService.findAllRequest();
		Request rq = new Request();
		rq.setRequestNumber("00" + (requests.size() + 1));
		rq.setCreateDate(new Date());
		rq.setStatus("In Progress");
		rq = requestService.saveRequest(rq);
		createRow(rq, requests.size() + 1);
	}

	public void setHome(HomeController homeController) {
		this.homeController = homeController;
	}

}
