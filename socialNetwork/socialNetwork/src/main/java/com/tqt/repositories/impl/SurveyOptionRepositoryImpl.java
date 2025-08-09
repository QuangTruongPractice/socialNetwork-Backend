/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.SurveyOption;
import com.tqt.repositories.SurveyOptionRepository;
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
public class SurveyOptionRepositoryImpl implements SurveyOptionRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public SurveyOption getSurveyOptionById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(SurveyOption.class, id);
    }

    @Override
    public void addOrUpdateSurveyOption(SurveyOption so) {
        Session s = this.factory.getObject().getCurrentSession();
        if (so.getId() == null) {
            s.persist(so);
        } else {
            s.merge(so);
        }
    }
    
}
