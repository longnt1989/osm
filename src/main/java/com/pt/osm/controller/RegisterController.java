package com.pt.osm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.SecurityUtils;
import com.pt.osm.model.User;
import com.pt.osm.service.UserService;

/**
 *
 * Sep 9, 2016
 *
 * @author VuD
 * @edit longnt
 *
 */

public class RegisterController extends SelectorComposer<Component> implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbEmail;
	@Wire
	private Textbox tbPassword;
	@Wire
	private Textbox tbPasswordConfirm;
	@Wire
	private Textbox tbName;
	@Wire
	private Textbox tbPhonenumber;
	@Wire
	private Button btnSubmit;
	@Wire
	private Label labelAll;
	@Wire
	private Label labelName;
	@Wire
	private Label labelEmail;
	@Wire
	private Label labelPhone;
	@Wire
	private Label labelPass;
	@Wire
	private Label labelConfirm;
	@Wire
	private Image imageDeleteName;
	@Wire
	private Image imageDeleteMail;
	@Wire
	private Image imageDeletePhone;
	@Wire
	private Image imageDeletePass;
	@Wire
	private Image imageDeleteConfirmPass;
	@Wire
	private Image imageShowPass;
	@Wire
	private Div divMain;
	@Wire
	private Image imageShowConfirmPass;
	private boolean clickIcon = false;
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
		init();

	}

	private void init() {
		btnSubmit.addEventListener(Events.ON_CLICK, this);
		imageDeleteName.setVisible(false);
		imageDeletePass.setVisible(false);
		imageDeleteMail.setVisible(false);
		imageDeletePhone.setVisible(false);
		imageDeleteConfirmPass.setVisible(false);
		imageShowPass.setVisible(false);
		imageShowConfirmPass.setVisible(false);
		addEvent(tbName, imageDeleteName, null);
		addEvent(tbEmail, imageDeleteMail, null);
		addEvent(tbPhonenumber, imageDeletePhone, null);
		addEvent(tbPassword, imageDeletePass, imageShowPass);
		addEvent(tbPasswordConfirm, imageDeleteConfirmPass, imageShowConfirmPass);
		addEventPass(imageShowPass, tbPassword);
		addEventPass(imageShowConfirmPass, tbPasswordConfirm);
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

	// add all event textbox, delete text
	private void addEvent(Textbox txb, Image image, Image imageVisble) {
		// Press enter call doRegister()
		if (!txb.equals(tbName)) {
			txb.addEventListener(Events.ON_OK, this);
		}
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

		// set flag clickIcon
		image.addEventListener(Events.ON_MOUSE_OVER, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				clickIcon = true;
			}
		});
		image.addEventListener(Events.ON_MOUSE_OUT, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				clickIcon = false;
			}
		});

		// hidden image delete when out text and don't click image
		txb.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				if (!clickIcon) {
					image.setVisible(false);
					if (imageVisble != null && !txb.getValue().trim().isEmpty()) {
						imageVisble.setVisible(true);
					}
				}
			}
		});
	}

	@Override
	public void onEvent(Event event) throws Exception {
		this.doRegister();
	}

	private void doRegister() {
		String email = tbEmail.getValue();
		String password = tbPassword.getValue();
		String name = tbName.getValue();
		String phonenumber = tbPhonenumber.getValue();
		String confirmPass = tbPasswordConfirm.getValue();
		if (email.isEmpty() && password.isEmpty() && name.isEmpty() && phonenumber.isEmpty() && confirmPass.isEmpty()) {
			labelAll.setValue("This is requirement");
			tbName.setSclass("textbox error");
			tbEmail.setSclass("textbox error");
			tbPassword.setSclass("textbox error");
			tbPasswordConfirm.setSclass("textbox error");
			tbPhonenumber.setSclass("textbox error");
			return;
		} else {
			labelAll.setValue("");
			labelName.setValue("");
			labelEmail.setValue("");
			labelPass.setValue("");
			labelPhone.setValue("");
			labelConfirm.setValue("");
			tbName.setSclass("textbox");
			tbEmail.setSclass("textbox");
			tbPassword.setSclass("textbox");
			tbPasswordConfirm.setSclass("textbox");
			tbPhonenumber.setSclass("textbox");
		}

		User user = new User();
		user.setEmail(email);
		user.setUsername(email);
		user.setUserType(User.UserType.ENGINEER.value());
		user.setPassword(password);
		user.setFirstName(name);
		user.setLastName("");
		user = userService.saveUser(user);
		if (user != null) {
			Executions.sendRedirect("/");
		}
	}

}
