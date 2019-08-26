package com.pt.osm.service;


import com.pt.osm.common.SecurityUtils;
import com.pt.osm.model.User;
import com.pt.osm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Transactional(readOnly = true)
    public User findById(long id) {
        return userRepository.findById(id).get();
    }
    
    public User getUser(String username, String pass) {
        return userRepository.findByUsernameAndPassword(username, pass);
    }
    
    public List<User> findAll(){
    	return userRepository.findAll();
    }

    public User saveUser(User user) {
        if(user.getId() == null){
            user.setPassword(SecurityUtils.encryptMd5(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

}