package com.pt.osm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.SecurityUtils;
import com.pt.osm.model.User;
import com.pt.osm.service.UserService;

@org.springframework.stereotype.Component
public class LoginController extends SelectorComposer<Component> implements EventListener<Event> {

	@Wire
	private Div parent;
	@Wire
	private Textbox tbuser;
	@Wire
	private Textbox tbpass;
	@Wire
	private Button btnLogin;
	@Wire
	private Label labelName;
	@Wire
	private Label labelPass;
	@Wire
	private Checkbox chbRemember;
	@Wire
	private Div divMain;
	@Wire
	private Div divBottom;
	@Wire
	private A aForgot;
	@Wire
	private A aLanguage;
	@Wire
	private Label lbRemember;
	@Wire
	private Image imageDeleteName;
	@Wire
	private Image imageDeletePass;
	@Wire
	private Image imageShowPass;
	@Wire
	private Label pDontAcc;
	@Wire
	private A aSignUp;
	@Autowired
	private UserService userService;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userService = OsmApplication.ctx.getBean(UserService.class);
		this.btnLogin.addEventListener(Events.ON_CLICK, this);
		divMain.addEventListener(Events.ON_OK, this);
		imageDeleteName.setVisible(false);
		imageDeletePass.setVisible(false);
		imageShowPass.setVisible(false);
		addEventPass(imageShowPass, tbpass);
		tbpass.setType("password");

		addEvent(tbuser, imageDeleteName, null);
		addEvent(tbpass, imageDeletePass, imageShowPass);

	}

	@Override
	public void onEvent(Event event) throws Exception {
		String nm = tbuser.getValue();
		String pd = tbpass.getValue();

		this.dologin(nm, pd);
	}

	private void dologin(String username, String pd) {
		String pass = SecurityUtils.encryptMd5(pd);
		System.out.println(pass);
		User user = userService.getUser(username, pass);
		if (user != null) {
			Executions.getCurrent().getSession().setAttribute("user", user);
			Executions.sendRedirect("home");
		}
	}

	private void addEvent(Textbox txb, Image image, Image imageVisble) {
		// Press enter call doRegister()
		txb.addEventListener(Events.ON_OK, this);

		// enable image delete when press text
		txb.addEventListener(Events.ON_CHANGING, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				if (!image.isVisible()) {
					image.setVisible(true);
					if (imageVisble != null) {
						imageVisble.setVisible(false);
					}
				}

			}
		});

		// enable image delete when focus text
		txb.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				if (!txb.getText().trim().isEmpty()) {
					image.setVisible(true);
				}
				if (imageVisble != null) {
					imageVisble.setVisible(false);
				}
			}
		});

		// clear text when click image delete
		image.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				txb.setValue("");
				image.setVisible(false);
				if (imageVisble != null) {
					imageVisble.setVisible(true);
				}
			}
		});

		// hidden image delete when out text and don't click image
		txb.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {

				image.setVisible(false);
				if (imageVisble != null && !txb.getValue().trim().isEmpty()) {
					imageVisble.setVisible(true);
				}

			}
		});
	}

	// add event show pass when click image
	private void addEventPass(Image image, Textbox textbox) {
		image.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				if (textbox.getType().equals("text")) {
					textbox.setType("password");
				} else {
					textbox.setType("text");
				}
			}
		});
	}

}