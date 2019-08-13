package com.pt.osm.controller;

import com.pt.osm.OsmApplication;
import com.pt.osm.component.DivChat;
import com.pt.osm.model.User;
import com.pt.osm.model.UserType;
import com.pt.osm.service.UserTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.Iterator;
import java.util.List;

public class UserTypeController extends SelectorComposer<Component> {
    private static final Logger logger = LoggerFactory.getLogger(UserTypeController.class);

    @Autowired
    UserTypeService userTypeService;

    @Wire
    private Panel userTypeDiv;

    @Wire
    private Label lbTeamLeaderName;
    @Wire
    private A btnAddRow;
    @Wire
    private Rows rowsGeneral;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        User user = (User) Executions.getCurrent().getSession().getAttribute("user");
        userTypeService = OsmApplication.ctx.getBean(UserTypeService.class);
        userTypeDiv.setAttribute("UserTypeController", this);
        loadForm();

        btnAddRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
            public void onEvent(Event arg0) throws Exception {
                Row row = new Row();
                row.setParent(rowsGeneral);

                Textbox txtCode = new Textbox();
                txtCode.setStyle("width:100%");
                txtCode.setParent(row);

                Textbox txtName = new Textbox();
                txtName.setStyle("width:100%");
                txtName.setParent(row);

                Textbox txtDescription = new Textbox();
                txtDescription.setStyle("width:100%");
                txtDescription.setParent(row);

                Div divButton = new Div();
                A aSave = createBtnSave();
                aSave.setParent(divButton);
                aSave.setVisible(true);

                A aEdit = createBtnEdit();
                aEdit.setParent(divButton);
                aEdit.setVisible(false);

                A aDelete = createBtnDelete();
                aDelete.setParent(divButton);
                aDelete.setVisible(false);

                A aCancel = createBtnCancel();
                aCancel.setParent(divButton);
                aCancel.setVisible(false);


                A aRemoveRow = createBtnCancel();
                aRemoveRow.setParent(divButton);
                aRemoveRow.setVisible(true);

                aDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        Messagebox.show("Delete this type. Are you sure?", "Confirm", Messagebox.YES | Messagebox.NO,
                                Messagebox.QUESTION, new EventListener<Event>() {
                                    @Override
                                    public void onEvent(final Event evt) throws InterruptedException {
                                        if (Messagebox.ON_YES.equals(evt.getName())) {
                                            UserType uType = userTypeService.getByCode(txtCode.getValue());
                                            userTypeService.delete(uType);
                                            rowsGeneral.removeChild(row);
                                        }
                                    }
                                });
                    }
                });

                aEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        txtName.setDisabled(false);
                        txtDescription.setDisabled(false);
                        aEdit.setVisible(false);
                        aDelete.setVisible(false);
                        aSave.setVisible(true);
                        aCancel.setVisible(true);
                    }
                });

                aSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        if(txtCode.getValue().trim().isEmpty()){
                            Messagebox.show("Code is empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        if(txtName.getValue().trim().isEmpty()){
                            Messagebox.show("Name is empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        UserType userType = userTypeService.getByCode(txtCode.getValue());
                        if(userType != null){
                            Messagebox.show("Type already exists", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        userType = new UserType();
                        userType.setCode(txtCode.getValue());
                        userType.setName(txtName.getText());
                        userType.setDescription(txtDescription.getText());
                        userTypeService.save(userType);

                        txtCode.setDisabled(true);
                        txtName.setDisabled(true);
                        txtDescription.setDisabled(true);

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                        aRemoveRow.setVisible(false);
                    }
                });

                aCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        UserType userType = userTypeService.getByCode(txtCode.getValue());
                        txtName.setValue(userType.getName());
                        txtDescription.setValue(userType.getDescription());

                        txtName.setDisabled(true);
                        txtDescription.setDisabled(true);

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                    }
                });

                aRemoveRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        rowsGeneral.removeChild(row);
                    }
                });

                divButton.setParent(row);
            }
        });

    }


    void loadForm() {
        try {
            List<UserType> userTypes = userTypeService.findAll();
            for (UserType userType : userTypes) {
                Row row = new Row();
                row.setParent(rowsGeneral);

                Textbox txtCode = new Textbox(userType.getCode());
                txtCode.setDisabled(true);
                txtCode.setStyle("width:100%");
                txtCode.setParent(row);

                Textbox txtName = new Textbox(userType.getName());
                txtName.setStyle("width:100%");
                txtName.setDisabled(true);
                txtName.setParent(row);

                Textbox txtDescription = new Textbox(userType.getDescription());
                txtDescription.setStyle("width:100%");
                txtDescription.setDisabled(true);
                txtDescription.setParent(row);

                Div divButton = new Div();

                A aSave = createBtnSave();
                aSave.setParent(divButton);
                aSave.setVisible(false);

                A aEdit = createBtnEdit();
                aEdit.setParent(divButton);

                A aDelete = createBtnDelete();
                aDelete.setParent(divButton);

                A aCancel = createBtnCancel();
                aCancel.setParent(divButton);
                aCancel.setVisible(false);


                aDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        Messagebox.show("Delete this type. Are you sure?", "Confirm", Messagebox.YES | Messagebox.NO,
                                Messagebox.QUESTION, new EventListener<Event>() {
                                    @Override
                                    public void onEvent(final Event evt) throws InterruptedException {
                                        if (Messagebox.ON_YES.equals(evt.getName())) {
                                            UserType uType = userTypeService.getByCode(userType.getCode());
                                            userTypeService.delete(uType);
                                            rowsGeneral.removeChild(row);
                                        }
                                    }
                                });
                    }
                });

                aEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        txtName.setDisabled(false);
                        txtDescription.setDisabled(false);
                        aEdit.setVisible(false);
                        aDelete.setVisible(false);
                        aSave.setVisible(true);
                        aCancel.setVisible(true);
                    }
                });

                aSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        userType.setName(txtName.getText());
                        userType.setDescription(txtDescription.getText());
                        userTypeService.save(userType);

                        txtName.setDisabled(true);
                        txtDescription.setDisabled(true);
                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                    }
                });

                aCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        txtName.setValue(userType.getName());
                        txtDescription.setValue(userType.getDescription());
                        txtName.setDisabled(true);
                        txtDescription.setDisabled(true);

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                    }
                });

                divButton.setParent(row);
            }
        } catch (Exception ex) {
            logger.error("loadForm error: " + ex.getMessage());
        }
    }


    A createBtnCancel() {
        A aCancel = new A("");
        Image imgCancel = new Image("/img/cancel.png");
        imgCancel.setParent(aCancel);
        imgCancel.setStyle("width: 25px; margin:5px");
        return aCancel;
    }

    A createBtnDelete() {
        A aDelete = new A("");
        Image imgDelete = new Image("/img/delete.png");
        imgDelete.setParent(aDelete);
        imgDelete.setStyle("width: 25px; margin:5px");
        return aDelete;
    }

    A createBtnEdit() {
        A aEdit = new A("");
        Image imgCancel = new Image("/img/edit.png");
        imgCancel.setParent(aEdit);
        imgCancel.setStyle("width: 25px; margin:5px");
        return aEdit;
    }

    A createBtnSave() {
        A aSave = new A("");
        Image imgCancel = new Image("/img/ok.png");
        imgCancel.setParent(aSave);
        imgCancel.setStyle("width: 25px; margin:5px");
        return aSave;
    }


}
