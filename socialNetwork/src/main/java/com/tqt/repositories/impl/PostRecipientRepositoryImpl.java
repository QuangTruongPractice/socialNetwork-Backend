/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.PostRecipient;
import com.tqt.repositories.PostRecipientRepository;
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
public class PostRecipientRepositoryImpl implements PostRecipientRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public void addOrUpdatePostRecipient(PostRecipient pr) {
        Session s = this.factory.getObject().getCurrentSession();
        if (pr.getId() == null) {
            s.persist(pr);
        } else {
            s.merge(pr);
        }
    }
}
