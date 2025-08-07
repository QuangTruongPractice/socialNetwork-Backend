/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.Notification;
import com.tqt.pojo.User;
import com.tqt.repositories.NotificationRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class NotificationServiceImpl implements NotificationService{
    
    @Autowired
    private NotificationRepository notiRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public List<Notification> getNotificationByUserId(int id) {
        return this.notiRepo.getNotificationByUserId(id);
    }
    
    @Override
    public Notification getNotificationById(int id){
        return this.notiRepo.getNotificationById(id);
    }

    @Override
    public void addOrUpdateNotification(Notification n) {
        this.notiRepo.addOrUpdateNotification(n);
    }
    
    public void read(int notificationId){
        Notification n = this.notiRepo.getNotificationById(notificationId);
        n.setRead(true);
        this.addOrUpdateNotification(n);
    }
    
}
