package com.pt.osm.service;


import com.pt.osm.model.UploadLog;
import com.pt.osm.repository.UploadLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadLogService {

    @Autowired
    private UploadLogRepository uploadLogRepository;

    public List<UploadLog> findByUsername(String username) {
        return uploadLogRepository.findByUsername(username);
    }

    public UploadLog findByName(String name) {
        return uploadLogRepository.findByName(name);
    }

    public UploadLog save(UploadLog uploadLog) {
        return uploadLogRepository.save(uploadLog);
    }

    public void delete(UploadLog uploadLog) {
        uploadLogRepository.delete(uploadLog);
    }

    public List<UploadLog> findAll() {
        return uploadLogRepository.findAll();
    }

    public List<UploadLog> searchRequest(String label,String username, Long requestId) {
        if(label != null){
            label = "%"+label+"%";
        }
        return uploadLogRepository.searchRequest(label, username, requestId);
    }


}