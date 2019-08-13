package com.pt.osm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;

import com.pt.osm.OsmApplication;
import com.pt.osm.model.Offer;
import com.pt.osm.model.Request;
import com.pt.osm.service.RequestService;

public class DevelopmentController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Panel developmentDiv;
	@Wire
	private Row rowDevelopment;

	@Autowired
	private RequestService requestService;


	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		developmentDiv.setAttribute("DevelopmentController", this);

	}

	public Panel getDevelopmentDiv() {
		return developmentDiv;
	}

	public void setRequest(Request request) {
		rowDevelopment.getChildren().clear();
		Label lb = new Label(request.getRequestNumber());
		lb.setParent(rowDevelopment);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("request", request);
		Panel developmentReqDiv = (Panel) Executions.createComponents("~./zul/develop_request.zul", null, params);
		developmentReqDiv.setParent(rowDevelopment);
		DevelopmentReqController developmentController = (DevelopmentReqController)developmentReqDiv.getAttribute("DevelopmentReqController");
		developmentController.setRequest(request);
	}
	
	public void setOffer(Request request) {

	}

}
