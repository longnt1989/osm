package com.pt.osm.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;

import com.pt.osm.model.User;

public class GermanSideController extends SelectorComposer<Component>{

	/**
	 * 
	 * German Side coding by HieuND1
	 */
	private static final long serialVersionUID = 1L;
    
	
	@Wire
	private Panel groupGermanSideDiv;

	@Wire
	private Label lbpGermanSideName;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
        User user = (User) Executions.getCurrent().getSession().getAttribute("user");
        groupGermanSideDiv.setAttribute("GermanSideController", this);

	}

}
