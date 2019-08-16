package com.pt.osm.component;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.idom.Attribute;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.A;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.Observer;
import com.pt.osm.model.Comment;
import com.pt.osm.model.User;
import com.pt.osm.service.RequestService;

public class DivChat extends Vlayout {

	@Autowired
	private RequestService requestService;

	public static String View_Request = "Request";
	public static String View_Order = "Order";
	public static String View_Offer = "Offer";
	public static String View_DataRequest = "DataRequest";
	public static String View_Budget = "BudgetRequest";
	public static String View_Payment = "Payment";
	private String key;

	public DivChat(long linkId, String typeView, int type) {
		String key1 = typeView + String.valueOf(linkId) + String.valueOf(type) + "1";
		String key2 = typeView + String.valueOf(linkId) + String.valueOf(type) + "2";
		key = key1;
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		this.setStyle("width:300px;bottom: 10px;position: absolute;background: white");
		Hlayout divTop = new Hlayout();
		divTop.setParent(this);
		divTop.setStyle("width:100%; height:50px; float:left;    border-bottom: 1px solid #d9d9d9;");
		Image img = new Image("/img/iconComment.png");
		img.setHeight("40px");
		img.setParent(divTop);
		Textbox txt = new Textbox();
		txt.setStyle("margin-top:3px");
		txt.setPlaceholder("Comment here");
		txt.setParent(divTop);
		txt.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				String chat = txt.getValue();
				Comment comment = new Comment();
				comment.setContent(chat);
				comment.setName("Long");
				comment.setTypeView(typeView);
				comment.setRequestId(linkId);
				comment.setType(type);
				comment.setCreateDate(new Date());
				if (linkId != -1) {
					comment = requestService.saveComment(comment);
				}

				Observer.fireChatEvent(key, comment);
				txt.setValue("");
			}
		});
		txt.setCtrlKeys("$#up$#down");
		txt.addEventListener(Events.ON_CTRL_KEY, new EventListener<KeyEvent>() {
			@Override
			public void onEvent(KeyEvent event) throws Exception {
				int keyCode = ((KeyEvent) event).getKeyCode();
				System.out.println(keyCode);
				if(event.isShiftKey()) {
					System.out.println(1);
				}

			}
		});
		
		ContentLayout center = new ContentLayout(key1, linkId, type, typeView);
		center.setParent(this);
		center.setStyle("width:100%; min-height:50px; float:left;border-bottom: 1px solid #d9d9d9;");

		ContentLayout center1 = new ContentLayout(key2, linkId, type, typeView);
		center1.setParent(this);
		center1.setStyle("width:100%; min-height:50px; float:left;border-bottom: 1px solid #d9d9d9;");

		Hlayout divBottom = new Hlayout();
		divBottom.setParent(this);
		divBottom.setStyle("width:100%; height:50px; float:left; text-align:center");
		org.zkoss.zhtml.A aCall = new org.zkoss.zhtml.A();
		aCall.setParent(divBottom);
		Image imgCall = new Image("/img/iconCall.png");
		imgCall.setStyle("width:50px;");
		imgCall.setParent(aCall);

		org.zkoss.zhtml.A aChat = new org.zkoss.zhtml.A();
		aChat.setParent(divBottom);
		Image iconChat = new Image("/img/iconChat.png");
		iconChat.setStyle("height:40px; margin-top:5px; margin-right:5px");
		iconChat.setParent(aChat);

		org.zkoss.zhtml.A aEmail = new org.zkoss.zhtml.A();
		aEmail.setParent(divBottom);
		Image imgEmail = new Image("/img/iconEmail.png");
		imgEmail.setStyle("height:40px; margin-top:5px; margin-right:5px");
		imgEmail.setParent(aEmail);

		A aCalenda = new A();
		aCalenda.setParent(divBottom);
		Image imgCalenda = new Image("/img/iconCalenda.jpg");
		imgCalenda.setStyle("height:35px; margin-top:8px");
		imgCalenda.setParent(aCalenda);
		aChat.setHref("skype:echo123?chat");
		aCall.setHref("skype:echo123?call");
		aEmail.setHref("mailto:your_mail@example.com");

		// Call Email
		aEmail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub

			}
		});

		// Call Calenda
		aCalenda.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				Window w = new Window();
				w.setWidth("800px");
				w.setHeight("600px");
				w.setTitle("Calenda");
				w.setClosable(true);
				w.setParent(DivChat.this);
				Iframe ifr = new Iframe(
						"https://calendar.google.com/calendar/embed?src=kk2veb34u8fmlasmr4mrqrt07s%40group.calendar.google.com&ctz=Asia%2FHo_Chi_Minh\" style=\"border: 0\" width=\"800\" height=\"600\" frameborder=\"0\" scrolling=\"no\"");
				ifr.setWidth("100%");
				ifr.setHeight("100%");
				ifr.setParent(w);
				w.doModal();

			}
		});

	}

}
