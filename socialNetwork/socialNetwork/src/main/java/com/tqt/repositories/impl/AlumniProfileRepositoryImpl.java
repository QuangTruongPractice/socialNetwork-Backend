/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.AlumniProfile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.tqt.repositories.AlumniProfileRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 *
 * @author Quang Truong
 */
@Repository
@Transactional
public class AlumniProfileRepositoryImpl implements AlumniProfileRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public AlumniProfile getAlumniProfileById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(AlumniProfile.class, id);
    }

    @Override
    public AlumniProfile getAlumniProfileByUserId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM AlumniProfile WHERE user.id = :userId";
        return session.createQuery(hql, AlumniProfile.class)
                .setParameter("userId", id)
                .uniqueResult();
    }

    @Override
    public AlumniProfile addAlumniProfile(AlumniProfile p) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(p);
        return p;
    }

    @Override
    public AlumniProfile updateAlumniProfile(AlumniProfile p) {
        Session s = this.factory.getObject().getCurrentSession();
        return (AlumniProfile) s.merge(p);
    }

}
