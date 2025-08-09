/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.LecturerProfile;
import com.tqt.repositories.LecturerProfileRepository;
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
public class LecturerProfileRepositoryImpl implements LecturerProfileRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public LecturerProfile getLecturerProfileById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(LecturerProfile.class, id);
    }

    @Override
    public LecturerProfile getLecturerProfileByUserId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM LecturerProfile WHERE user.id = :userId";
        return session.createQuery(hql, LecturerProfile.class)
                .setParameter("userId", id)
                .uniqueResult();
    }

    @Override
    public LecturerProfile addLecturerProfile(LecturerProfile p) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(p);
        return p;
    }

    @Override
    public LecturerProfile updateLecturerProfile(LecturerProfile p) {
        Session s = this.factory.getObject().getCurrentSession();
        return (LecturerProfile) s.merge(p); 
    }
}
