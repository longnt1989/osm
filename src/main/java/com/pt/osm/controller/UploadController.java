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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
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
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UploadController  extends SelectorComposer<Component> {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    UploadLogService uploadLogService;

    @WireVariable
    protected Properties adminProps;

    @Wire
    private Button btnUpload;
    @Wire
    private Rows rowsGeneral;

    User user = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        user = (User) Executions.getCurrent().getSession().getAttribute("user");
        if (user == null) {
            Executions.sendRedirect("/");
            return;
        }
        uploadLogService = OsmApplication.ctx.getBean(UploadLogService.class);
        loadForm();


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

            if(media == null){
                Messagebox.show("Please select a file", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            logger.info("uploading");
            InputStream fin = media.getStreamData();
            in = new BufferedInputStream(fin);

            File baseDir = new File(PropertiesParams.folderUploadUrl);

            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            String labelFile = media.getName();
            String[] fileTypeArr = labelFile.split("\\.");
            String fileName = user.getUsername()+sdf.format(new Date())+"."+fileTypeArr[fileTypeArr.length-1];
            File file = new File(PropertiesParams.folderUploadUrl + fileName);

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
        }catch (Exception ex){
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



    void loadForm() {
        try {
            List<UploadLog> uploadLogs = uploadLogService.findByUsername(user.getUsername());
            for (UploadLog uploadLog : uploadLogs) {
                Row row = new Row();
                row.setParent(rowsGeneral);
                Label lbName = new Label(uploadLog.getLabel());
                lbName.setParent(row);

                Div divButton = new Div();
                A aSave = createBtnSave();
                aSave.setParent(divButton);
                aSave.setVisible(true);

                aSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                    public void onEvent(Event arg0) throws Exception {
                        //FileUtils.copyURLToFile(new URL("file:///"+PropertiesParams.folderUploadUrl+uploadLog.getName()), new File("C:\\Users\\minht\\Downloads\\"+uploadLog.getLabel()));

                        FileInputStream inputStream;
                        try {
                            File dosfile = new File(PropertiesParams.folderUploadUrl+uploadLog.getName());
                            if (dosfile.exists()) {
                                inputStream = new FileInputStream(dosfile);
                                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), uploadLog.getLabel());
                            } else{
                                Messagebox.show("Sorry, but the DosPath is Invalid", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                            }

                        } catch (FileNotFoundException e) {
                            logger.error("file download error: "+e.getMessage());
                        }
                    }
                });



                divButton.setParent(row);
            }
        } catch (Exception ex) {
            logger.error("loadForm error: " + ex.getMessage());
        }
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


    A createBtnSave() {
        A aSave = new A("download");
        /*Image imgCancel = new Image("/img/ok.png");
        imgCancel.setParent(aSave);
        imgCancel.setStyle("width: 25px; margin:5px");*/
        return aSave;
    }

}
