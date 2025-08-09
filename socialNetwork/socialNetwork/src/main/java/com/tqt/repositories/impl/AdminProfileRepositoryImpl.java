/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.AdminProfile;
import com.tqt.repositories.AdminProfileRepository;
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
public class AdminProfileRepositoryImpl implements AdminProfileRepository{

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public AdminProfile getAdminProfileById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(AdminProfile.class, id);
    }

    @Override
    public AdminProfile getAdminProfileByUserId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM AdminProfile WHERE user.id = :userId";
        return session.createQuery(hql, AdminProfile.class)
                .setParameter("userId", id)
                .uniqueResult();
    }

    @Override
    public AdminProfile addAdminProfile(AdminProfile p) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(p);
        return p;
    }

    @Override
    public AdminProfile updateAdminProfile(AdminProfile p) {
        Session s = this.factory.getObject().getCurrentSession();
        return (AdminProfile) s.merge(p); 
    }
    
}
