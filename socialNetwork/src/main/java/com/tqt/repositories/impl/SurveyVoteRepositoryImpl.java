/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.SurveyVote;
import com.tqt.repositories.SurveyVoteRepository;
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
public class SurveyVoteRepositoryImpl implements SurveyVoteRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public boolean existsByUserIdAndPostId(int userId, int postId) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(v.id) FROM SurveyVote v WHERE v.user.id = :userId AND v.option.post.id = :postId";
        Long count = session.createQuery(hql, Long.class)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public void addOrUpdateSurveyVote(SurveyVote sv) {
        Session s = this.factory.getObject().getCurrentSession();
        if (sv.getId() == null) {
            s.persist(sv);
        } else {
            s.merge(sv);
        }
    }

}
