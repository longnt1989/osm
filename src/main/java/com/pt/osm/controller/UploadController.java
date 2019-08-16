package com.pt.osm.controller;

import com.pt.osm.OsmApplication;
import com.pt.osm.common.PropertiesParams;
import com.pt.osm.model.UploadLog;
import com.pt.osm.model.User;
import com.pt.osm.service.UploadLogService;
import com.pt.osm.service.UserService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UploadController extends SelectorComposer<Component> {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    UploadLogService uploadLogService;

    @WireVariable
    protected Properties adminProps;

    @Wire
    private Button btnUpload;
    @Wire
    private Rows rowsGeneral;
    @Wire
    private Row rowSearchUpload;
    @Wire
    private Textbox txtSearchUpload;

    User user = null;
    private static String fileNameDownload = null;
    private static String fileLabelDownload = null;
    private static int flagSearch = 0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        user = (User) Executions.getCurrent().getSession().getAttribute("user");
        if (user == null) {
            Executions.sendRedirect("/");
            return;
        }
        uploadLogService = OsmApplication.ctx.getBean(UploadLogService.class);
        loadForm(null);
        rowSearchUpload.setVisible(false);

        txtSearchUpload.addEventListener(Events.ON_OK, new EventListener() {
                    public void onEvent(final Event pEvent) {
                        String typingMessage = txtSearchUpload.getValue();
                        loadForm(typingMessage);
                    }
                });

    }

    @Command("search")
    public void onSearch() {
        try {
            if (flagSearch == 0) {
                rowSearchUpload.setVisible(true);
                flagSearch = 1;
            } else {
                rowSearchUpload.setVisible(false);
                flagSearch = 0;
                loadForm(null);
            }
        } catch (Exception ex) {
            logger.error("file download onSearch error: " + ex.getMessage());
        }
    }


    @Command("upload")
    public void onUpload(@BindingParam("media") Media media) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            uploadLogService = OsmApplication.ctx.getBean(UploadLogService.class);
            user = (User) Executions.getCurrent().getSession().getAttribute("user");
            if (user == null) {
                Executions.sendRedirect("/");
                return;
            }

            if (media == null) {
                Messagebox.show("Please select a file", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            logger.info("uploading");
            InputStream fin = media.getStreamData();
            in = new BufferedInputStream(fin);

            File baseDir = new File(getPathForderUpload());

            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            String labelFile = media.getName();
            String[] fileTypeArr = labelFile.split("\\.");
            String fileName = user.getUsername() + sdf.format(new Date()) + "." + fileTypeArr[fileTypeArr.length - 1];
            File file = new File(getPathForderUpload() + "/" + fileName);

            OutputStream fout = new FileOutputStream(file);
            out = new BufferedOutputStream(fout);
            byte buffer[] = new byte[1024];
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);
                ch = in.read(buffer);
            }
            // write to database
            UploadLog uploadLog = new UploadLog();
            uploadLog.setName(fileName);
            uploadLog.setLabel(labelFile);
            uploadLog.setUsername(user.getUsername());
            uploadLogService.save(uploadLog);
            logger.info("sucessed upload :" + media.getName());
            //reloadPage
            loadForm(null);
        } catch (Exception ex) {
            logger.error("onUpload error" + ex.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Command("download")
    public void onDownload() {
        try {
            File dosfile = new File(getPathForderUpload() + "/" + fileNameDownload);
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), fileLabelDownload);
            } else {
                Messagebox.show("Sorry, but the path is invalid", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception ex) {
            logger.error("file download error: " + ex.getMessage());
        }
    }

    @Command("delete")
    public void onDelete() {
        try {
            uploadLogService = OsmApplication.ctx.getBean(UploadLogService.class);
            File dosfile = new File(getPathForderUpload() + "/" + fileNameDownload);
            Messagebox.show("Delete this file. Are you sure?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        if (dosfile.delete()) {
                            System.out.println(fileNameDownload + " is deleted!");
                            UploadLog uploadLog = uploadLogService.findByName(fileNameDownload);
                            uploadLogService.delete(uploadLog);
                            //reload form
                            loadForm(null);
                        } else {
                            Messagebox.show("Sorry, but the path is invalid", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                        }
                    }
                }
            });

        } catch (Exception ex) {
            logger.error("file download error: " + ex.getMessage());
        }
    }


    void loadForm(String fileName) {
        try {
            rowsGeneral.getChildren().clear();
            uploadLogService = OsmApplication.ctx.getBean(UploadLogService.class);
            List<UploadLog> uploadLogs = uploadLogService.searchRequest(fileName, null, null);
            for (UploadLog uploadLog : uploadLogs) {
                addRowFile(uploadLog);
            }

        } catch (Exception ex) {
            logger.error("loadForm error: " + ex.getMessage());
        }
    }

    void addRowFile(UploadLog uploadLog) {
        Row row = new Row();
        row.setParent(rowsGeneral);
        Image img = new Image();
        img.setSrc("/img/file.png");
        img.setParent(row);

        final int[] flagColor = {0};
        Div divButton = new Div();
        A aSave = createBtnSave(uploadLog.getLabel());
        aSave.setParent(divButton);
        aSave.setVisible(true);
        aSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
            public void onEvent(Event arg0) throws Exception {
                FileInputStream inputStream;
                try {
                    List<Row> rows = rowsGeneral.getChildren();
                    for (Row row : rows) {
                        row.setStyle("background-color: white");
                    }
                    if (flagColor[0] == 0) {
                        row.setStyle("background-color: white");
                        flagColor[0] = 1;
                        fileNameDownload = null;
                        fileLabelDownload = null;
                    } else {
                        row.setStyle("background-color: orange");
                        flagColor[0] = 0;
                        fileNameDownload = uploadLog.getName();
                        fileLabelDownload = uploadLog.getLabel();
                    }

                } catch (Exception e) {
                    logger.error("choose file error: " + e.getMessage());
                }
            }
        });

        divButton.setParent(row);
    }

    public static void downloadFileFromURL(String urlString, File destination) {
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    A createBtnSave(String label) {
        A aSave = new A(label);
        aSave.setStyle("text-decoration : none; color: black; ");
        /*Image imgCancel = new Image("/img/ok.png");
        imgCancel.setParent(aSave);
        imgCancel.setStyle("width: 25px; margin:5px");*/
        return aSave;
    }

    public String getPathForderUpload() {
        String username = System.getProperty("user.home");
        File baseDir = new File(username + "/upload");

        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir.getAbsolutePath();
    }

}
