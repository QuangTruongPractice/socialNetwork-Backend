/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Follow;
import com.tqt.pojo.User;
import com.tqt.repositories.FollowRepository;
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
public class FollowRepositoryImpl implements FollowRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<User> getFollowingByFollowerId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT f.following FROM Follow f WHERE f.follower.id = :id";
        return session.createQuery(hql, User.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<User> getFollowerByFollowingId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT f.follower FROM Follow f WHERE f.following.id = :id";
        return session.createQuery(hql, User.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Follow getFollowByFollowerIdAndFollowingId(int followerId, int followingId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT f FROM Follow f WHERE f.follower.id =:followerId AND f.following.id =:followingId";
        return session.createQuery(hql, Follow.class)
                .setParameter("followerId", followerId)
                .setParameter("followingId", followingId)
                .uniqueResult();
    }

    @Override
    public Follow addFollow(Follow f) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(f);
        return f;
    }

    @Override
    public void deleteFollow(int followerId, int followingId) {
        Session s = this.factory.getObject().getCurrentSession();
        Follow f = this.getFollowByFollowerIdAndFollowingId(followerId, followingId);
        s.remove(f);
    }

    @Override
    public boolean checkFollowing(int followerId, int followingId) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :followerId AND f.following.id = :followingId";
        Long count = session.createQuery(hql, Long.class)
                .setParameter("followerId", followerId)
                .setParameter("followingId", followingId)
                .getSingleResult();
        return count > 0;
    }

}
