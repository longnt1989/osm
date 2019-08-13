package com.pt.osm.controller;

import com.pt.osm.model.User;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;

public class CEOController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Panel ceoDiv;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
        User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		ceoDiv.setAttribute("CEOController", this);

	}



}
