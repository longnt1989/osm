package com.pt.osm.controller;

import com.pt.osm.OsmApplication;
import com.pt.osm.model.User;
import com.pt.osm.model.UserType;
import com.pt.osm.service.UserService;
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

import java.util.ArrayList;
import java.util.List;

public class UserController extends SelectorComposer<Component> {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Wire
    private Panel userDiv;

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
        userService = OsmApplication.ctx.getBean(UserService.class);
        userDiv.setAttribute("UserController", this);
        loadForm();

        btnAddRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
            public void onEvent(Event arg0) throws Exception {
                Row row = new Row();
                row.setParent(rowsGeneral);

                Textbox txtUsername = new Textbox();
                txtUsername.setStyle("width:100%");
                txtUsername.setParent(row);

                Textbox txtEmail = new Textbox();
                txtEmail.setStyle("width:100%");
                txtEmail.setParent(row);

                Textbox txtSkype = new Textbox();
                txtSkype.setStyle("width:100%");
                txtSkype.setParent(row);

                Textbox txtFirstName = new Textbox();
                txtFirstName.setStyle("width:100%");
                txtFirstName.setParent(row);


                Textbox txtLastName = new Textbox();
                txtLastName.setStyle("width:100%");
                txtLastName.setParent(row);

                Combobox cbUserType = createComboUserType();
                cbUserType.setParent(row);

                Combobox cbSide = createComboSide();
                cbSide.setParent(row);

                Combobox cbStatus = createComboStatus();
                cbStatus.setParent(row);

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


                A aSaveEdit = createBtnSave();
                aSaveEdit.setParent(divButton);
                aSaveEdit.setVisible(false);

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
                                            User userDelete = userService.findByUsername(txtUsername.getValue());
                                            if(userDelete != null){
                                                userService.delete(userDelete);
                                            }
                                            rowsGeneral.removeChild(row);
                                        }
                                    }
                                });
                    }
                });

                aEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {

                        txtEmail.setDisabled(false);
                        txtSkype.setDisabled(false);
                        txtFirstName.setDisabled(false);
                        txtLastName.setDisabled(false);
                        cbUserType.setDisabled(false);
                        cbSide.setDisabled(false);
                        cbStatus.setDisabled(false);

                        aEdit.setVisible(false);
                        aDelete.setVisible(false);
                        aSave.setVisible(false);
                        aCancel.setVisible(true);
                        aSaveEdit.setVisible(true);
                    }
                });

                aSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        if(txtUsername.getValue().trim().isEmpty()){
                            Messagebox.show("Username isn't empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        if(txtFirstName.getValue().trim().isEmpty()){
                            Messagebox.show("First name isn't empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        if(txtLastName.getValue().trim().isEmpty()){
                            Messagebox.show("Last name isn't empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        User user = userService.findByUsername(txtUsername.getValue());
                        if(user != null){
                            Messagebox.show("Username already exists", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        user = new User();
                        user.setUsername(txtUsername.getValue());
                        user.setPassword("123456");
                        user.setEmail(txtEmail.getValue());
                        user.setSkype(txtSkype.getValue());
                        user.setFirstName(txtFirstName.getValue());
                        user.setLastName(txtLastName.getValue());
                        String userType = cbUserType.getValue().trim();
                        user.setUserType(userType);
                        user.setSide(cbSide.getValue());
                        int status = User.Status.ACTIVE.value();
                        if(User.Status.INACTIVE.name().equals(cbStatus.getValue())){
                            status = User.Status.INACTIVE.value();
                        }
                        user.setStatus(status);
                        userService.saveUser(user);

                        txtUsername.setDisabled(true);
                        txtEmail.setDisabled(true);
                        txtSkype.setDisabled(true);
                        txtFirstName.setDisabled(true);
                        txtLastName.setDisabled(true);
                        cbUserType.setDisabled(true);
                        cbSide.setDisabled(true);
                        cbStatus.setDisabled(true);

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                        aRemoveRow.setVisible(false);
                    }
                });

                aSaveEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        if(txtUsername.getValue().trim().isEmpty()){
                            Messagebox.show("Username isn't empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        if(txtFirstName.getValue().trim().isEmpty()){
                            Messagebox.show("First name isn't empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        if(txtLastName.getValue().trim().isEmpty()){
                            Messagebox.show("Last name isn't empty", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        User user = userService.findByUsername(txtUsername.getValue());
                        if(user == null){
                            Messagebox.show("User not exists", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            return;
                        }
                        user.setEmail(txtEmail.getValue());
                        user.setSkype(txtSkype.getValue());
                        user.setFirstName(txtFirstName.getValue());
                        user.setLastName(txtLastName.getValue());
                        String userType = cbUserType.getValue().trim();
                        user.setUserType(userType);
                        user.setSide(cbSide.getValue());
                        int status = User.Status.ACTIVE.value();
                        if(User.Status.INACTIVE.name().equals(cbStatus.getValue())){
                            status = User.Status.INACTIVE.value();
                        }
                        user.setStatus(status);
                        userService.saveUser(user);

                        txtUsername.setDisabled(true);
                        txtEmail.setDisabled(true);
                        txtSkype.setDisabled(true);
                        txtFirstName.setDisabled(true);
                        txtLastName.setDisabled(true);
                        cbUserType.setDisabled(true);
                        cbSide.setDisabled(true);
                        cbStatus.setDisabled(true);

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                        aRemoveRow.setVisible(false);
                        aSaveEdit.setVisible(true);
                    }
                });

                aCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                        aSaveEdit.setVisible(false);
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
            List<User> users = userService.findAll();
            for (User user : users) {
                Row row = new Row();
                row.setParent(rowsGeneral);
                Textbox txtUsername = new Textbox(user.getUsername());
                txtUsername.setDisabled(true);
                txtUsername.setStyle("width:100%");
                txtUsername.setParent(row);

                Textbox txtEmail = new Textbox(user.getEmail());
                txtEmail.setDisabled(true);
                txtEmail.setStyle("width:100%");
                txtEmail.setParent(row);

                Textbox txtSkype = new Textbox(user.getSkype());
                txtSkype.setDisabled(true);
                txtSkype.setStyle("width:100%");
                txtSkype.setParent(row);

                Textbox txtFirstName = new Textbox(user.getFirstName());
                txtFirstName.setDisabled(true);
                txtFirstName.setStyle("width:100%");
                txtFirstName.setParent(row);


                Textbox txtLastName = new Textbox(user.getLastName());
                txtLastName.setDisabled(true);
                txtLastName.setStyle("width:100%");
                txtLastName.setParent(row);

                Combobox cbUserType = createComboUserType();
                ListModel<String> comboitems = cbUserType.getModel();
                cbUserType.setDisabled(true);
                cbUserType.setParent(row);

                Combobox cbSide = createComboSide();
                cbSide.setDisabled(true);
                cbSide.setParent(row);

                Combobox cbStatus = createComboStatus();
                cbStatus.setDisabled(true);
                cbStatus.setParent(row);


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
                                            User uExist = userService.findByUsername(txtUsername.getValue());
                                            if(uExist != null) {
                                                userService.delete(uExist);
                                            }
                                            rowsGeneral.removeChild(row);
                                        }
                                    }
                                });
                    }
                });

                aEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        txtEmail.setDisabled(false);
                        txtSkype.setDisabled(false);
                        txtFirstName.setDisabled(false);
                        txtLastName.setDisabled(false);
                        cbUserType.setDisabled(false);
                        cbSide.setDisabled(false);
                        cbStatus.setDisabled(false);

                        aEdit.setVisible(false);
                        aDelete.setVisible(false);
                        aSave.setVisible(true);
                        aCancel.setVisible(true);
                    }
                });

                aSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        user.setUsername(txtUsername.getValue());
                        user.setPassword("123456");
                        user.setEmail(txtEmail.getValue());
                        user.setSkype(txtSkype.getValue());
                        user.setFirstName(txtFirstName.getValue());
                        user.setLastName(txtLastName.getValue());
                        String userType = cbUserType.getValue().trim();
                        user.setUserType(userType);
                        user.setSide(cbSide.getValue());
                        int status = User.Status.ACTIVE.value();
                        if(User.Status.INACTIVE.name().equals(cbStatus.getValue())){
                            status = User.Status.INACTIVE.value();
                        }
                        user.setStatus(status);
                        userService.saveUser(user);

                        txtEmail.setDisabled(true);
                        txtSkype.setDisabled(true);
                        txtFirstName.setDisabled(true);
                        txtLastName.setDisabled(true);
                        cbUserType.setDisabled(true);
                        cbSide.setDisabled(true);
                        cbStatus.setDisabled(true);

                        aEdit.setVisible(true);
                        aDelete.setVisible(true);
                        aSave.setVisible(false);
                        aCancel.setVisible(false);
                    }
                });

                aCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        txtUsername.setValue(user.getUsername());
                        txtEmail.setValue(user.getEmail());
                        txtSkype.setValue(user.getSkype());
                        txtFirstName.setValue(user.getFirstName());
                        txtLastName.setValue(user.getLastName());

                        txtEmail.setDisabled(true);
                        txtSkype.setDisabled(true);
                        txtFirstName.setDisabled(true);
                        txtLastName.setDisabled(true);
                        cbUserType.setDisabled(true);
                        cbSide.setDisabled(true);
                        cbStatus.setDisabled(true);

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



    Combobox createComboStatus(){
        Combobox cbStatus = new Combobox();
        List<String> lstStatus = new ArrayList<>();
        lstStatus.add(User.Status.ACTIVE.name());
        lstStatus.add(User.Status.INACTIVE.name());
        ListModelArray<String> reasonsModel = new ListModelArray<String>(lstStatus);
        reasonsModel.addToSelection(User.Status.ACTIVE.name());
        cbStatus.setModel(reasonsModel);
        cbStatus.setStyle("width:100%");
        return cbStatus;
    }

    Combobox createComboSide(){
        Combobox cbSide = new Combobox();
        List<String> lstStatus = new ArrayList<>();
        lstStatus.add(User.Side.GE.value());
        lstStatus.add(User.Side.VN.value());
        ListModelArray<String> reasonsModel = new ListModelArray<String>(lstStatus);
        reasonsModel.addToSelection(User.Status.ACTIVE.name());
        cbSide.setModel(reasonsModel);
        cbSide.setStyle("width:100%");
        return cbSide;
    }

    Combobox createComboUserType(){
        Combobox cbSide = new Combobox();
        List<String> lstStatus = new ArrayList<>();
        lstStatus.add(User.UserType.ADMIN.value());
        lstStatus.add(User.UserType.GERMAN.value());
        lstStatus.add(User.UserType.CEO.value());
        lstStatus.add(User.UserType.TEAMLEADER.value());
        lstStatus.add(User.UserType.GROUPLEADER.value());
        lstStatus.add(User.UserType.ENGINEER.value());
        ListModelArray<String> reasonsModel = new ListModelArray<String>(lstStatus);
        reasonsModel.addToSelection(User.Status.ACTIVE.name());
        cbSide.setModel(reasonsModel);
        cbSide.setStyle("width:100%");
        return cbSide;
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
