package com.pt.osm.controller;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Panel;

import com.pt.osm.component.DivChat;
import com.pt.osm.model.User;

public class HomeController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Div parent, divChat;
	@Wire
	private A aBoard, aDevelopment,aData,aDetail;
	@WireVariable
	Desktop desktop;
	@Wire
	A aCreateReq;
	private GeneralController generalController;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.enableServerPush(true);
		generalController = loadRequest();
		aCreateReq.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				generalController.createRequest();
			};
		});
		aData.setVisible(false);
		aDetail.setVisible(false);
		aBoard.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				generalController = loadRequest();
				aBoard.setStyle(
						"background:#c3e1f7;height:45px; margin-top:2px; padding:10px; padding-top:8px; text-decoration:none; margin-left:10px");
				aDevelopment.setStyle(
						"height:45px; margin-top:2px; padding:10px; padding-top:10px; float:right; text-decoration:none; margin-right:20px");
			};
		});
		aDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				showRequestDetail();
				aDetail.setStyle("width:15px; height:15px; border-radius:15px; background:#0064ed;float:right;margin-right:15px;margin-top: 15px ");
				aData.setStyle("width:15px; height:15px; border-radius:15px; border:1px solid;float:right;margin-right:15px;margin-top: 15px ");
			};
		});
		
		aData.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				parent.getChildren().clear();
				Include curren = new Include("~./zul/dataofrequest.zul");
				curren.setHeight("auto");
				curren.setParent(parent);
				Include offer = new Include("~./zul/dataofoffer.zul");
				offer.setParent(parent);
				aData.setStyle("width:15px; height:15px; border-radius:15px; background:#0064ed;float:right;margin-right:15px;margin-top: 15px ");
				aDetail.setStyle("width:15px; height:15px; border-radius:15px; border:1px solid;float:right;margin-right:15px;margin-top: 15px ");
			};
		});
	}

	private GeneralController loadRequest() {
		parent.getChildren().clear();
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		Map<String, Object> params = new HashMap<String, Object>();
		Panel general = (Panel) Executions.createComponents("~./zul/general.zul", null, params);
		general.setParent(parent);

		Include curren = new Include("~./zul/currenttask.zul");
		curren.setParent(parent);
		GeneralController generalController = ((GeneralController) general.getAttribute("GeneralController"));
		DivChat divChatG = new DivChat(-1, DivChat.View_Request, 0);
		divChatG.setParent(divChat);
		generalController.setHome(this);
		aData.setVisible(false);
		aDetail.setVisible(false);
		return generalController;
	}

	public void showRequestDetail() {
		parent.getChildren().clear();
		Include curren = new Include("~./zul/development.zul");
		curren.setParent(parent);
		//aCreateReq.setVisible(false);
		aData.setVisible(true);
		aDetail.setVisible(true);
		aBoard.setStyle(
				"height:45px; margin-top:2px; padding:10px; padding-top:8px; text-decoration:none; margin-left:10px");
		aDevelopment.setStyle(
				"background:#c3e1f7;height:45px; margin-top:2px; padding:10px; padding-top:10px; float:right; text-decoration:none; margin-right:20px");
	}

}
