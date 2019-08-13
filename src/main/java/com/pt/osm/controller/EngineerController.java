package com.pt.osm.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;

import com.pt.osm.model.User;

public class EngineerController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Panel enginnerDiv;

	@Wire
	private Label lbEnginerName;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		enginnerDiv.setAttribute("EngineerController", this);

	}

	public void setUser(User user) {
		if (user != null) {
			lbEnginerName.setValue("Name: " + user.getFirstName()+" "+user.getLastName());
		}
	}

}
