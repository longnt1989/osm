package com.pt.osm.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.Observer;
import com.pt.osm.component.ContentLayout;
import com.pt.osm.model.Comment;
import com.pt.osm.model.GroupChat;
import com.pt.osm.model.MapChat;
import com.pt.osm.model.Request;
import com.pt.osm.model.User;
import com.pt.osm.service.RequestService;
import com.pt.osm.service.UserService;

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
	Combobox cboGroupChat;
	@Wire
	A lb1, lb2, lb3, aAddGroup, aDelete3, aDelete2, aDelete1, aAddMem1, aAddMem2, aAddMem3;
	@Wire
	Vlayout vChat;
	@Wire
	Image imgAdd;
	private GeneralController generalController;
	private Request request;
	private String typeView = "", key, key1, key2, key3;
	private int type = 0;
	private ContentLayout center, center1, center2;
	private User user;
	private UserService userService;
	private RequestService requestService;
	private List<GroupChat> lstChat;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.enableServerPush(true);
		userService = OsmApplication.ctx.getBean(UserService.class);
		user = (User) Executions.getCurrent().getSession().getAttribute("user");
		requestService = OsmApplication.ctx.getBean(RequestService.class);
		if (user == null) {
			Executions.sendRedirect("/");
		}
		lstChat = new ArrayList<GroupChat>();
		generalController = loadRequest();
		aCreateReq.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				generalController.createRequest();
			};
		});

		aNoneTask.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				key = key1;
				loadUINoneTask();
			};
		});
		aDelete1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				try {
					requestService.deleteGroupChat(lstChat.get(0));
				} catch (Exception e) {
				}
				loadUIChatNT();
			};
		});
		aDelete2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				try {
					requestService.deleteGroupChat(lstChat.get(1));
				} catch (Exception e) {
				}
				loadUIChatNT();
			};
		});
		aDelete3.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				try {
					requestService.deleteGroupChat(lstChat.get(2));
				} catch (Exception e) {
				}
				loadUIChatNT();
			};
		});
		aTask.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				key = key1;
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
		txtChat.setPlaceholder("Chat here");
		txtChat.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				String chat = txtChat.getValue();
				Comment comment = new Comment();
				comment.setContent(chat);
				comment.setName(user.toString());
				comment.setTypeView(typeView);
				long linkId = -1;
				if (key.equals(key1)) {
					linkId = lstChat.get(0).getId();
				} else {
					if (key.equals(key2)) {
						linkId = lstChat.get(1).getId();
					} else {
						if (key.equals(key3)) {
							linkId = lstChat.get(2).getId();
						}
					}
				}
				comment.setRequestId(linkId);
				comment.setType(type);
				comment.setCreateDate(new Date());
				comment = requestService.saveComment(comment);

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
						loadHeader(div3, div1, div2, key3);
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
				if (uiChat1.getVflex() == null
						|| (uiChat1.getVflex() != null && uiChat2.getVflex() != null && uiChat3.getVflex() != null)) {
					fullChat1();
				} else {
					fullChat();
				}

			}
		});

		lb2.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (uiChat2.getVflex() == null
						|| (uiChat1.getVflex() != null && uiChat2.getVflex() != null && uiChat3.getVflex() != null)) {
					fullChat2();
				} else {
					fullChat();
				}
			}
		});

		lb3.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (uiChat3.getVflex() == null
						|| (uiChat1.getVflex() != null && uiChat2.getVflex() != null && uiChat3.getVflex() != null)) {
					fullChat3();
				} else {
					fullChat();
				}
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

		center = new ContentLayout();
		center.setParent(divChat1);
		center.setStyle("width:100%; float:left;height:100%;overflow-y: auto;");

		center1 = new ContentLayout();
		center1.setParent(divChat2);
		center1.setStyle("width:100%;  float:left;height:100%;overflow-y: auto;");

		center2 = new ContentLayout();
		center2.setParent(divChat3);
		center2.setStyle("width:100%; float:left;height:100%;overflow-y: auto;");

		loadUINoneTask();

		aAddGroup.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				Popup popup = new Popup();
				popup.setParent(parent);
				popup.setWidth("300px");
				popup.setHeight("420px");
				popup.setStyle("border:1px solid #14852a");
				Textbox txtName = new Textbox();
				txtName.setParent(popup);
				txtName.setPlaceholder("Type name of the chat");
				txtName.setStyle("float:left; width:100%");
				Label lb = new Label("Members");
				lb.setStyle("float:left; width:100%;margin-top: 10px;");
				lb.setParent(popup);
				Listbox listbox = new Listbox();
				listbox.setStyle("float:left; width:100%; height:230px; margin-top:10px");
				listbox.setParent(popup);
				Listitem listitem = new Listitem(user.toString(), user.getId());
				listitem.setParent(listbox);
				Combobox combobox = new Combobox();
				combobox.setPlaceholder("Add member");
				combobox.setAutodrop(true);
				combobox.setButtonVisible(false);
				combobox.setStyle("float:left; width:100%; margin-top:10px");
				combobox.setParent(popup);
				List<User> lstUsers = userService.findAll();
				combobox.setModel(new ListModelList<User>(lstUsers));
				combobox.addEventListener(Events.ON_OK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						if (combobox.getSelectedItem() != null) {
							User user = combobox.getSelectedItem().getValue();
							boolean add = true;
							for (int i = 0; i < listbox.getItemCount(); i++) {
								Listitem itListitem = listbox.getItemAtIndex(i);
								long idUser = itListitem.getValue();
								if (idUser == user.getId()) {
									add = false;
								}
							}
							if (add) {
								Listitem listitem = new Listitem(user.toString(), user.getId());
								listitem.setParent(listbox);
							}
							combobox.setSelectedItem(null);
						}

					}
				});
				Button btn = new Button("Save");
				btn.setStyle("float:right; margin-top:10px");
				btn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						GroupChat groupChat = new GroupChat();
						groupChat.setName(txtName.getValue());
						groupChat.setTime(new Date());
						if (aNoneTask.getStyle().contains("0064ed")) {
							groupChat.setType(0);
						} else {
							groupChat.setType(1);
							groupChat.setLinkId(request.getId());
						}
						groupChat.setIndexChat(1);
						groupChat = requestService.saveGroupChat(groupChat);
						for (int i = 0; i < listbox.getItemCount(); i++) {
							Listitem itListitem = listbox.getItemAtIndex(i);
							long idUser = itListitem.getValue();
							MapChat mapChat = new MapChat();
							mapChat.setGroupId(groupChat.getId());
							mapChat.setUserId(idUser);
							requestService.saveMapChat(mapChat);
						}
						for (GroupChat gc : lstChat) {
							if (gc.getIndexChat() == 1) {
								gc.setIndexChat(2);
								requestService.saveGroupChat(gc);
							} else {
								if (gc.getIndexChat() == 2) {
									gc.setIndexChat(3);
									requestService.saveGroupChat(gc);
								} else {
									if (gc.getIndexChat() == 3) {
										gc.setIndexChat(-1);
										requestService.saveGroupChat(gc);
									}
								}
							}
						}
						loadUIChatNT();
						popup.close();
					}
				});
				btn.setParent(popup);
				popup.open(aAddGroup, "start_before");
			};
		});

		aAddMem1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				popupAddMem(lstChat.get(0), divChat1);
			};
		});
		aAddMem2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				popupAddMem(lstChat.get(1), divChat2);
			};
		});

		aAddMem3.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				popupAddMem(lstChat.get(2), divChat3);
			};
		});

		cboGroupChat.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				GroupChat groupChat = cboGroupChat.getSelectedItem().getValue();
				if (groupChat != null) {
					groupChat.setIndexChat(1);
					requestService.saveGroupChat(groupChat);
					for (GroupChat gc : lstChat) {
						if (gc.getIndexChat() == 1) {
							gc.setIndexChat(2);
							requestService.saveGroupChat(gc);
						} else {
							if (gc.getIndexChat() == 2) {
								gc.setIndexChat(3);
								requestService.saveGroupChat(gc);
							} else {
								if (gc.getIndexChat() == 3) {
									gc.setIndexChat(-1);
									requestService.saveGroupChat(gc);
								}
							}
						}
					}
					loadUIChatNT();
				}

			}
		});

	}

	private GeneralController loadRequest() {
		parent.getChildren().clear();

		Map<String, Object> params = new HashMap<String, Object>();
		Panel general = (Panel) Executions.createComponents("~./zul/general.zul", null, params);
		general.setParent(parent);

		Include curren = new Include("~./zul/currenttask.zul");
		curren.setParent(parent);
		GeneralController generalController = ((GeneralController) general.getAttribute("GeneralController"));
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
		loadUITask();
	}

	private void loadUIChat(int type) {
		List<GroupChat> lst = new ArrayList<GroupChat>();
		if (type == 0) {
			lst = requestService.findByTypeAndLinkId(type, 0);
		} else {
			if (request != null) {
				lst = requestService.findByTypeAndLinkId(type, request.getId());
				if (lst.size() == 0) {
					GroupChat groupChat = new GroupChat();
					groupChat.setName("Budget");
					groupChat.setTime(new Date());
					groupChat.setType(1);
					groupChat.setLinkId(request.getId());
					groupChat.setIndexChat(2);
					groupChat = requestService.saveGroupChat(groupChat);
					groupChat = new GroupChat();
					groupChat.setName("Technic");
					groupChat.setTime(new Date());
					groupChat.setType(1);
					groupChat.setLinkId(request.getId());
					groupChat.setIndexChat(1);
					groupChat = requestService.saveGroupChat(groupChat);
				}
				lst = requestService.findByTypeAndLinkId(type, request.getId());
			}

		}
		lstChat = new ArrayList<GroupChat>();
		for (int i = 0; i < lst.size();) {
			if (lst.get(i).getIndexChat() > 0) {
				lstChat.add(lst.get(i));
				lst.remove(i);
			} else {
				i++;
			}
		}
		Collections.sort(lstChat, new Comparator<GroupChat>() {

			@Override
			public int compare(GroupChat arg0, GroupChat arg1) {
				if (arg0.getIndexChat() > arg1.getIndexChat()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
		cboGroupChat.setModel(new ListModelList<GroupChat>(lst));

		switch (lstChat.size()) {
		case 0:
			uiChat1.setVisible(false);
			uiChat2.setVisible(false);
			uiChat3.setVisible(false);

			break;
		case 1:
			uiChat2.setVisible(false);
			uiChat3.setVisible(false);
			uiChat1.setVisible(true);
			lb1.setLabel(lstChat.get(0).getName());
			key1 = String.valueOf(lstChat.get(0).getId());
			center.setGroupId(lstChat.get(0).getId());
			break;
		case 2:
			uiChat3.setVisible(false);
			uiChat1.setVisible(true);
			uiChat2.setVisible(true);
			lb1.setLabel(lstChat.get(0).getName());
			lb2.setLabel(lstChat.get(1).getName());
			key1 = String.valueOf(lstChat.get(0).getId());
			key2 = String.valueOf(lstChat.get(1).getId());
			center.setGroupId(lstChat.get(0).getId());
			center1.setGroupId(lstChat.get(1).getId());
			break;
		default:
			uiChat3.setVisible(true);
			uiChat1.setVisible(true);
			uiChat2.setVisible(true);
			lb1.setLabel(lstChat.get(0).getName());
			lb2.setLabel(lstChat.get(1).getName());
			lb3.setLabel(lstChat.get(2).getName());
			key1 = String.valueOf(lstChat.get(0).getId());
			key2 = String.valueOf(lstChat.get(1).getId());
			center.setGroupId(lstChat.get(0).getId());
			center1.setGroupId(lstChat.get(1).getId());
			key3 = String.valueOf(lstChat.get(2).getId());
			center2.setGroupId(lstChat.get(2).getId());
			this.key = key1;
			break;
		}
	}

	private void loadUIChatNT() {
		if (aNoneTask.getStyle().contains("0064ed")) {
			loadUINoneTask();
		} else {
			loadUITask();
		}
	}

	private void loadUINoneTask() {

		imgAdd.setSrc("/img/iconGroupAddN.png");
		aNoneTask.setStyle(
				"width:15px; height:15px; border-radius:15px;background:#0064ed; border:1px solid;float:left;margin-left:15px;margin-top: 13px ");
		aTask.setStyle(
				"width:15px; height:15px; border-radius:15px;border:1px solid; float:left;margin-left:20px;margin-top: 13px ");
		div1.setStyle("background: #c4987a;width: 300px;height: 40px;");
		div2.setStyle("background: #855e42;width: 300px;height: 40px;");
		div3.setStyle("background: #855e42;width: 300px;height: 40px;");
		center.getChildren().clear();
		center1.getChildren().clear();
		center2.getChildren().clear();
		fullChat();
		loadUIChat(0);
	}

	private void loadUITask() {
		if (request == null) {
			return;
		}
		imgAdd.setSrc("/img/iconGroupAddT.png");
		aNoneTask.setStyle(
				"width:15px; height:15px; border-radius:15px;border:1px solid;float:left;margin-left:15px;margin-top: 13px ");
		aTask.setStyle(
				"width:15px; height:15px; border-radius:15px;background:#0064ed;  float:left;margin-left:20px;margin-top: 13px ");
		div1.setStyle("background: #84bd8f;width: 300px;height: 40px;");
		div2.setStyle("background: #14852a;width: 300px;height: 40px;");
		div3.setStyle("background: #14852a;width: 300px;height: 40px;");
		center.getChildren().clear();
		center1.getChildren().clear();
		center2.getChildren().clear();
		this.key = key1;
		fullChat();
		loadUIChat(1);
	}

	private void fullChat() {
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
		if (aTask.getStyle().contains("0064ed")) {
			divSelect.setStyle("background: #84bd8f;width: 300px;height: 40px;");
			div2.setStyle("background: #14852a;width: 300px;height: 40px;");
			div3.setStyle("background: #14852a;width: 300px;height: 40px;");
		} else {
			divSelect.setStyle("background: #c4987a;width: 300px;height: 40px;");
			div2.setStyle("background: #855e42;width: 300px;height: 40px;");
			div3.setStyle("background: #855e42;width: 300px;height: 40px;");
		}
		if (key.equals(key1) && (uiChat2.getHeight() != null || uiChat3.getHeight() != null)) {
			fullChat1();
		}
		if (key.equals(key2) && (uiChat1.getHeight() != null || uiChat3.getHeight() != null)) {
			fullChat2();
		}
		if (key.equals(key3) && (uiChat2.getHeight() != null || uiChat1.getHeight() != null)) {
			fullChat3();
		}
	}

	private void popupAddMem(GroupChat groupChat, Div div) {
		Popup popup = new Popup();
		popup.setParent(parent);
		popup.setWidth("300px");
		popup.setHeight("250px");
		popup.setStyle("border:1px solid #14852a");
		Combobox combobox = new Combobox();
		combobox.setPlaceholder("Add member");
		combobox.setAutodrop(true);
		combobox.setButtonVisible(false);
		combobox.setStyle("float:left; width:100%;");
		combobox.setParent(popup);
		Listbox listbox = new Listbox();
		listbox.setStyle("float:left; width:100%; height:190px;");
		listbox.setParent(popup);
		List<MapChat> mapChats = requestService.findByGroupId(groupChat.getId());
		for (MapChat mc : mapChats) {
			User user = userService.findById(mc.getUserId());
			Listitem listitem = new Listitem();
			listitem.setValue(user.getId());
			Listcell listcell = new Listcell(user.toString());
			listcell.setParent(listitem);
			Image img = new Image("/img/delete.png");
			img.setParent(listcell);
			img.setStyle("float:right");
			listitem.setParent(listbox);
			img.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					requestService.deleteMapChat(mc);
					listbox.removeChild(listitem);
				}
			});
		}

		List<User> lstUsers = userService.findAll();
		combobox.setModel(new ListModelList<User>(lstUsers));
		combobox.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (combobox.getSelectedItem() != null) {
					User user = combobox.getSelectedItem().getValue();
					boolean add = true;
					for (int i = 0; i < listbox.getItemCount(); i++) {
						Listitem itListitem = listbox.getItemAtIndex(i);
						long idUser = itListitem.getValue();
						if (idUser == user.getId()) {
							add = false;
						}
					}
					if (add) {
						MapChat mapChat = new MapChat();
						mapChat.setGroupId(groupChat.getId());
						mapChat.setUserId(user.getId());
						requestService.saveMapChat(mapChat);
						Listitem listitem = new Listitem(user.toString(), user.getId());
						listitem.setParent(listbox);
					}
					combobox.setSelectedItem(null);
				}

			}
		});

		popup.open(div, "overlap_end");
	}

	private void fullChat1() {
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

	private void fullChat2() {
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

	private void fullChat3() {
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

}
