/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.enums.ReactionType;
import com.tqt.pojo.Reaction;
import com.tqt.repositories.ReactionRepository;
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
public class ReactionRepositoryImpl implements ReactionRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Reaction> getReactionByPostId(int postId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Reaction r WHERE r.post.id = :postId AND r.reactionType != :none "
                + "ORDER BY r.createdDate ASC";

        return session.createQuery(hql, Reaction.class)
                .setParameter("postId", postId)
                .setParameter("none", ReactionType.NONE)
                .getResultList();
    }

    @Override
    public Reaction getReactionByUserAndPost(int userId, int postId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Reaction r WHERE r.user.id = :userId AND r.post.id = :postId";
        return session.createQuery(hql, Reaction.class)
                      .setParameter("userId", userId)
                      .setParameter("postId", postId)
                      .uniqueResult();
    }

    @Override
    public Reaction addReaction(Reaction r) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(r);
        return r;
    }

    @Override
    public Reaction updateReaction(Reaction r) {
        Session s = this.factory.getObject().getCurrentSession();
        return (Reaction) s.merge(r);
    }
    
}
