/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.Notification;
import java.util.List;

/**
 *
 * @author Quang Truong
 */
public interface NotificationService {
    List<Notification> getNotificationByUserId(int id);
    Notification getNotificationById(int id);
    void addOrUpdateNotification(Notification n);
    void read(int notificationId);
}
