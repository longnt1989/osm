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
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

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
	private Div parent, divChat, divChat1, divChat2, divChat3, div1, div2, div3, uiChat3, uiChat2, uiChat1;
	@Wire
	private A aBoard, aDevelopment, aData, aDetail, aNoneTask, aTask;
	@WireVariable
	Desktop desktop;
	@Wire
	A aCreateReq;
	@Wire
	Textbox txtChat;
	@Wire
	A lb1, lb2, lb3;
	@Wire
	Vlayout vChat;
	private GeneralController generalController;
	private Request request;
	private String typeView, key, key1, key2, key3;
	private long linkId = -1;
	private int type = 0;
	private ContentLayout center, center1, center2;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.enableServerPush(true);
		generalController = loadRequest();
		aCreateReq.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				generalController.createRequest();
			};
		});

		aNoneTask.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				loadUINoneTask();
			};
		});
		aTask.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				loadUITask();
			};
		});
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
		txtChat.setCtrlKeys("@1@2@3^1^2^3");
		txtChat.addEventListener(Events.ON_CTRL_KEY, new EventListener<KeyEvent>() {
			@Override
			public void onEvent(KeyEvent event) throws Exception {
				int keyCode = ((KeyEvent) event).getKeyCode();
				if (event.isCtrlKey()) {
					switch (keyCode) {
					case 49:
						loadHeader(div1, div2, div3, key1);
						break;
					case 50:
						loadHeader(div2, div1, div3, key2);
						break;
					case 51:
						loadHeader(div3, div1, div3, key3);
						break;

					default:
						break;
					}
				} else {
					if (event.isAltKey()) {
						switch (keyCode) {
						case 49:
							loadUINoneTask();
							break;
						case 50:
							loadUITask();
							break;
						default:
							break;
						}

					}
				}
			}
		});

		lb1.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				key = key1;
				uiChat2.setVflex(null);
				uiChat3.setVflex(null);
				uiChat2.setHeight("40px");
				uiChat3.setHeight("40px");
				uiChat1.setHeight(null);
				uiChat1.setVflex("1");
				divChat1.invalidate();
				divChat2.invalidate();
				divChat3.invalidate();
				vChat.invalidate();
			}
		});

		lb2.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				key = key2;
				uiChat1.setVflex(null);
				uiChat3.setVflex(null);
				uiChat1.setHeight("40px");
				uiChat3.setHeight("40px");
				uiChat2.setHeight(null);
				uiChat2.setVflex("1");
				divChat1.invalidate();
				divChat2.invalidate();
				divChat3.invalidate();
				vChat.invalidate();
			}
		});

		lb3.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				key = key3;
				uiChat2.setVflex(null);
				uiChat1.setVflex(null);
				uiChat2.setHeight("40px");
				uiChat1.setHeight("40px");
				uiChat3.setHeight(null);
				uiChat3.setVflex("1");
				divChat1.invalidate();
				divChat2.invalidate();
				divChat3.invalidate();
				vChat.invalidate();
			}
		});
		
		lb1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				loadHeader(div1, div2, div3, key1);
			
			}
		});

		lb2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				loadHeader(div2, div1, div3, key2);
			
			}
		});

		lb3.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				loadHeader(div3, div2, div1, key3);
				
			}
		});

		key1 = typeView + String.valueOf(linkId) + String.valueOf(type) + "1";
		key2 = typeView + String.valueOf(linkId) + String.valueOf(type) + "2";
		key3 = typeView + String.valueOf(linkId) + String.valueOf(type) + "3";
		this.key = key1;
		center = new ContentLayout(key1, linkId, type, typeView);
		center.setParent(divChat1);
		center.setStyle("width:100%; float:left;height:100%;overflow-y: auto;");

		center1 = new ContentLayout(key2, linkId, type, typeView);
		center1.setParent(divChat2);
		center1.setStyle("width:100%;  float:left;height:100%;overflow-y: auto;");

		center2 = new ContentLayout(key3, linkId, type, typeView);
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

	private void loadUINoneTask() {
		aNoneTask.setStyle(
				"width:15px; height:15px; border-radius:15px;background:#0064ed; border:1px solid;float:left;margin-left:15px;margin-top: 13px ");
		aTask.setStyle(
				"width:15px; height:15px; border-radius:15px;border:1px solid; float:left;margin-left:20px;margin-top: 13px ");
		div1.setStyle("background: #c4987a;width: 300px;height: 40px;");
		div2.setStyle("background: #855e42;width: 300px;height: 40px;");
		div3.setStyle("background: #855e42;width: 300px;height: 40px;");
		lb3.setLabel("None task 3");
		lb2.setLabel("None task 2");
		lb1.setLabel("None task 1");
		center.getChildren().clear();
		center1.getChildren().clear();
		center2.getChildren().clear();
		this.key = key1;
		uiChat2.setHeight(null);
		uiChat2.setVflex("1");
		uiChat3.setHeight(null);
		uiChat3.setVflex("1");
		uiChat1.setHeight(null);
		uiChat1.setVflex("1");
		divChat1.invalidate();
		divChat2.invalidate();
		divChat3.invalidate();
		vChat.invalidate();
	}

	private void loadUITask() {
		aNoneTask.setStyle(
				"width:15px; height:15px; border-radius:15px;border:1px solid;float:left;margin-left:15px;margin-top: 13px ");
		aTask.setStyle(
				"width:15px; height:15px; border-radius:15px;background:#0064ed;  float:left;margin-left:20px;margin-top: 13px ");
		div1.setStyle("background: #84bd8f;width: 300px;height: 40px;");
		div2.setStyle("background: #14852a;width: 300px;height: 40px;");
		div3.setStyle("background: #14852a;width: 300px;height: 40px;");
		lb3.setLabel("Static");
		lb2.setLabel("Budget");
		lb1.setLabel("Technic");
		center.getChildren().clear();
		center1.getChildren().clear();
		center2.getChildren().clear();
		this.key = key1;
		uiChat2.setHeight(null);
		uiChat2.setVflex("1");
		uiChat3.setHeight(null);
		uiChat3.setVflex("1");
		uiChat1.setHeight(null);
		uiChat1.setVflex("1");
		divChat1.invalidate();
		divChat2.invalidate();
		divChat3.invalidate();
		vChat.invalidate();
	}

	private void loadHeader(Div divSelect, Div div2, Div div3, String keySelect) {
		key = keySelect;
		if(aTask.getStyle().contains("0064ed")) {
			divSelect.setStyle("background: #84bd8f;width: 300px;height: 40px;");
			div2.setStyle("background: #14852a;width: 300px;height: 40px;");
			div3.setStyle("background: #14852a;width: 300px;height: 40px;");
		}else {
			divSelect.setStyle("background: #c4987a;width: 300px;height: 40px;");
			div2.setStyle("background: #855e42;width: 300px;height: 40px;");
			div3.setStyle("background: #855e42;width: 300px;height: 40px;");
		}
	}

}
