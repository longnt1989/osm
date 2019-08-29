package com.pt.osm.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.IObserver;
import com.pt.osm.common.Observer;
import com.pt.osm.model.Comment;
import com.pt.osm.service.RequestService;

public class ContentLayout extends Vlayout implements IObserver {
	private String key = "";

	@Autowired
	private RequestService requestService;

	public ContentLayout() {
		super();
		requestService = OsmApplication.ctx.getBean(RequestService.class);
	}

	public void setGroupId(long linkId) {
		removeEventListener();
		this.key = String.valueOf(linkId);
		addEventListener();
		this.getChildren().clear();
		List<Comment> lst = requestService.findByLinkId(linkId);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		for (Comment comment : lst) {
			loadComment(df, comment);
		}
		
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
						if (key.equals(ContentLayout.this.key)) {
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

	private void loadComment(DateFormat df, Comment comment) {
		Hlayout div = new Hlayout();
		div.setParent(this);
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
		Clients.scrollIntoView(div);
	}
}
