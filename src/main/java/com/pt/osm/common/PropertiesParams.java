package com.pt.osm.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesParams {
    public static String folderUploadUrl;

    private void setFolderUploadUrl(String folderUploadUrl){
        this.folderUploadUrl = folderUploadUrl;
    }
}
