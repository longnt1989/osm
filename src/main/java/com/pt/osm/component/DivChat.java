package com.pt.osm.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.IObserver;
import com.pt.osm.common.Observer;
import com.pt.osm.model.Comment;
import com.pt.osm.model.User;
import com.pt.osm.service.RequestService;

public class DivChat extends Vlayout implements IObserver {

	// private User user;
	private Vlayout center;
	private String key;
	@Autowired
	private RequestService requestService;

	public static String View_Request = "Request";
	public static String View_Order = "Order";
	public static String View_Offer = "Offer";
	public static String View_DataRequest = "DataRequest";
	public static String View_Budget = "BudgetRequest";
	public static String View_Payment = "Payment";

	public DivChat(long linkId, String typeView, int type) {
		this.key = typeView + String.valueOf(linkId) + String.valueOf(type);
		User user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		addEventListener();
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
				comment.setName(user.getFirstName() + " " + user.getLastName());
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

		center = new Vlayout();
		center.setParent(this);
		center.setStyle("width:100%; min-height:50px; float:left;border-bottom: 1px solid #d9d9d9;");

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

		List<Comment> lst = requestService.findByRequestIdAndTypeAndTypeView(linkId, type, typeView);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		for (Comment comment : lst) {
			loadComment(df, comment);
		}
	}

	private void loadComment(DateFormat df, Comment comment) {
		Hlayout div = new Hlayout();
		div.setParent(center);
		div.setStyle("width:100%;padding-bottom: 10px;border-bottom: 1px solid #d9d9d9;");
		Image imgFlag = new Image("/img/iconFlagVietnam.png");
		imgFlag.setHeight("40px");
		imgFlag.setParent(div);
		Vlayout vlayout = new Vlayout();
		vlayout.setParent(div);
		Label lb1 = new Label(comment.getName() + ", " + df.format(comment.getCreateDate()));
		lb1.setStyle("font-size: 12px;color: gray;");
		lb1.setParent(vlayout);
		Label lb = new Label(comment.getContent());
		lb.setStyle("width:85%; float:left");
		lb.setParent(vlayout);
	}

	@Override
	public void addEventListener() {
		Observer.addListener(key, this);

	}

	@Override
	public void removeEventListener() {
		Observer.removeListener(key, this);

	}

	@Override
	public void onEvent(String key, Object value) {
		try {
			if (this.getDesktop() == null) {
				removeEventListener();
			} else {
				Executions.schedule(this.getDesktop(), new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						if (key.equals(DivChat.this.key)) {
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:ss");
							Comment comment = (Comment) value;
							loadComment(df, comment);
						}

					}
				}, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isAlive() {
		boolean result = false;
		if (this.getDesktop() != null) {
			result = true;
		}
		return result;
	}
}
