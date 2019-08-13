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
	@WireVariable
	Desktop desktop;
	@Wire
	A aCreateReq;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.enableServerPush(true);
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		Map<String, Object> params = new HashMap<String, Object>();
		Panel general = (Panel) Executions.createComponents("~./zul/general.zul", null, params);
		general.setParent(parent);
		GeneralController generalController = ((GeneralController) general.getAttribute("GeneralController"));
		DivChat divChatG = new DivChat(-1, DivChat.View_Request, 0);
		divChatG.setParent(divChat);
		aCreateReq.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				generalController.createRequest();
			};
		});
	}

}
