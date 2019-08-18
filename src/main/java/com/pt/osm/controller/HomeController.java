package com.pt.osm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import com.pt.osm.common.Observer;
import com.pt.osm.component.ContentLayout;
import com.pt.osm.component.DivChat;
import com.pt.osm.model.Comment;
import com.pt.osm.model.Request;
import com.pt.osm.model.User;

public class HomeController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Div parent, divChat, divChat1, divChat2, divChat3;
	@Wire
	private A aBoard, aDevelopment, aData, aDetail;
	@WireVariable
	Desktop desktop;
	@Wire
	A aCreateReq;
	@Wire
	Textbox txtChat;
	private GeneralController generalController;
	private Request request;
	private String typeView, key, key1, key2, key3;
	private long linkId = -1;
	private int type = 0;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.enableServerPush(true);
		generalController = loadRequest();
		aCreateReq.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				generalController.createRequest();
			};
		});
//		aData.setVisible(false);
//		aDetail.setVisible(false);
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
				showRequestDetail(request);
				aDetail.setStyle(
						"width:15px; height:15px; border-radius:15px; background:#0064ed;float:right;margin-right:15px;margin-top: 15px ");
				aData.setStyle(
						"width:15px; height:15px; border-radius:15px; border:1px solid;float:right;margin-right:15px;margin-top: 15px ");
			};
		});

		aData.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				parent.getChildren().clear();

				Map<String, Object> params = new HashMap<String, Object>();
				Panel general = (Panel) Executions.createComponents("~./zul/dataofrequest.zul", null, params);
				general.setParent(parent);
				DevelopmentReqController generalController = ((DevelopmentReqController) general
						.getAttribute("DevelopmentReqController"));
				generalController.setRequest(request);

				Panel dataOffer = (Panel) Executions.createComponents("~./zul/dataofoffer.zul", null, params);
				dataOffer.setParent(parent);
				DevelopmentOfferController offerController = ((DevelopmentOfferController) dataOffer
						.getAttribute("DevelopmentOfferController"));
				offerController.setRequest(request);

				aData.setStyle(
						"width:15px; height:15px; border-radius:15px; background:#0064ed;float:right;margin-right:15px;margin-top: 15px ");
				aDetail.setStyle(
						"width:15px; height:15px; border-radius:15px; border:1px solid;float:right;margin-right:15px;margin-top: 15px ");
			};
		});
		typeView = DivChat.View_Request;
		txtChat.setPlaceholder("Chat here");
		txtChat.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				String chat = txtChat.getValue();
				Comment comment = new Comment();
				comment.setContent(chat);
				comment.setName("Long");
				comment.setTypeView(typeView);
				comment.setRequestId(linkId);
				comment.setType(type);
				comment.setCreateDate(new Date());
//				if (linkId != -1) {
//					comment = requestService.saveComment(comment);
//				}

				Observer.fireChatEvent(key, comment);
				txtChat.setValue("");
			}
		});
		txtChat.setCtrlKeys("@1@2@3");
		txtChat.addEventListener(Events.ON_CTRL_KEY, new EventListener<KeyEvent>() {
			@Override
			public void onEvent(KeyEvent event) throws Exception {
				int keyCode = ((KeyEvent) event).getKeyCode();
				switch (keyCode) {
				case 49:
					key = key1;
					break;
				case 50:
					key = key2;
					break;
				case 51:
					key = key3;
					break;

				default:
					break;
				}

			}
		});

		key1 = typeView + String.valueOf(linkId) + String.valueOf(type) + "1";
		key2 = typeView + String.valueOf(linkId) + String.valueOf(type) + "2";
		key3 = typeView + String.valueOf(linkId) + String.valueOf(type) + "3";
		this.key = key1;
		ContentLayout center = new ContentLayout(key1, linkId, type, typeView);
		center.setParent(divChat1);
		center.setStyle("width:100%; float:left;height:100%;overflow-y: auto;");

		ContentLayout center1 = new ContentLayout(key2, linkId, type, typeView);
		center1.setParent(divChat2);
		center1.setStyle("width:100%;  float:left;height:100%;overflow-y: auto;");

		ContentLayout center2 = new ContentLayout(key3, linkId, type, typeView);
		center2.setParent(divChat3);
		center2.setStyle("width:100%; float:left;height:100%;overflow-y: auto;");
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
//		DivChat divChatG = new DivChat(-1, DivChat.View_Request, 0);
//		divChatG.setParent(divChat);
		generalController.setHome(this);
		aDetail.setStyle(
				"width:15px; height:15px; border-radius:15px; background:#0064ed;float:right;margin-right:15px;margin-top: 15px ");
		aData.setStyle(
				"width:15px; height:15px; border-radius:15px; border:1px solid;float:right;margin-right:15px;margin-top: 15px ");
		aData.setVisible(false);
		aDetail.setVisible(false);
		return generalController;
	}

	public void showRequestDetail(Request request) {
		this.request = request;
		parent.getChildren().clear();
		Include curren = new Include("~./zul/development.zul");

		curren.setParent(parent);
		// aCreateReq.setVisible(false);
		aData.setVisible(true);
		aDetail.setVisible(true);
		aBoard.setStyle(
				"height:45px; margin-top:2px; padding:10px; padding-top:8px; text-decoration:none; margin-left:10px");
		aDevelopment.setStyle(
				"background:#c3e1f7;height:45px; margin-top:2px; padding:10px; padding-top:10px; float:right; text-decoration:none; margin-right:20px");
	}

}
