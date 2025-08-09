/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Notification;
import com.tqt.repositories.NotificationRepository;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Quang Truong
 */
@Repository
@Transactional
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Notification> getNotificationByUserId(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT n FROM Notification n WHERE n.user.id = :id ORDER BY n.id DESC";
        return s.createQuery(hql, Notification.class)
                .setParameter("id", id)
                .getResultList();
    }
    
    @Override
    public Notification getNotificationById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Notification.class, id);
    }

    @Override
    public void addOrUpdateNotification(Notification n) {
        Session s = this.factory.getObject().getCurrentSession();
        if (n.getId() == null) {
            s.persist(n);
        } else {
            s.merge(n);
        }
    }

    

}
