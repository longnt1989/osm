package com.pt.osm.service;


import com.pt.osm.controller.UserTypeController;
import com.pt.osm.model.UserType;
import com.pt.osm.repository.UserTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {
    private static final Logger logger = LoggerFactory.getLogger(UserTypeService.class);

    @Autowired
    UserTypeRepository userTypeRepository;


    public UserType save(UserType userType) {
        try {
            return userTypeRepository.save(userType);
        }catch (Exception ex){
            logger.error(userType.toString()+" - error: "+ex.getMessage());
            return null;
        }
    }

    public List<UserType> findAll(){
        return userTypeRepository.findAll();
    }

    public UserType getByCode(String code){
        return userTypeRepository.getByCode(code);
    }

    public void delete(UserType userType){
        userTypeRepository.delete(userType);
    }

}