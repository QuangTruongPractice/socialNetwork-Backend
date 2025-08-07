/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Post;
import com.tqt.pojo.User;
import com.tqt.repositories.PostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    private static final int PAGE_SIZE = 6;

    @Override
    public List<Post> getPosts(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Post> q = b.createQuery(Post.class);
        Root root = q.from(Post.class);
        q.select(root);

        Join<Post, User> userJoin = root.join("user", JoinType.INNER);

        if (params != null) {
            List<Predicate> predcates = new ArrayList<>();

            String userName = params.get("userName");
            if (userName != null && !userName.isEmpty()) {
                Expression<String> fullName = b.concat(userJoin.get("firstName"), b.concat(" ", userJoin.get("lastName")));
                predcates.add(b.like(b.lower(fullName), "%" + userName.toLowerCase() + "%"));
            }

            String createdDate = params.get("createdAt");
            if (createdDate != null && !createdDate.isEmpty()) {
                predcates.add(b.equal(root.get("createdAt"), LocalDate.parse(createdDate)));
            }

            q.where(predcates.toArray(Predicate[]::new));

            q.orderBy(b.desc(root.get(params.getOrDefault("sortBy", "id"))));
        }
        Query query = s.createQuery(q);

        if (params != null) {
            String page = params.get("page");
            if (page != null) {
                int start = (Integer.parseInt(page) - 1) * PAGE_SIZE;

                query.setFirstResult(start);
                query.setMaxResults(PAGE_SIZE);
            }
        }
        return query.getResultList();
    }
    
    @Override
    public Integer getTotalPages(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(p) FROM Post p";
        Query query = s.createQuery(hql, Long.class);
        Long count = (Long)query.getSingleResult();
        return (int) Math.ceil(count * 1.0 / PAGE_SIZE);
    }


    @Override
    public Post getPostById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Post.class, id);
    }
    
    @Override
    public List<Post> getPostByUserId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Post p WHERE user.id = :userId ORDER BY p.createdAt DESC";
        return session.createQuery(hql, Post.class)
                .setParameter("userId", id)
                .getResultList();
    }

    @Override
    public void addOrUpdatePost(Post p) {
        Session s = this.factory.getObject().getCurrentSession();
        if (p.getId() == null) {
            s.persist(p);
        } else {
            s.merge(p);
        }
    }
    
    @Override
    public void deletePost(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Post p = this.getPostById(id);
        s.remove(p);
    } 

}
