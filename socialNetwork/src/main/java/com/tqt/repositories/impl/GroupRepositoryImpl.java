/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Group;
import com.tqt.repositories.GroupRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
public class GroupRepositoryImpl implements GroupRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    private static final int PAGE_SIZE = 10;

    @Override
    public List<Group> getGroups(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Group> q = b.createQuery(Group.class);
        Root root = q.from(Group.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predcates = new ArrayList<>();

            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predcates.add(b.like(root.get("name"), "%" + name + "%"));
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
        String hql = "SELECT COUNT(g) FROM Group g";
        Query query = s.createQuery(hql, Long.class);
        Long count = (Long)query.getSingleResult();
        return (int) Math.ceil(count * 1.0 / PAGE_SIZE);
    }

    @Override
    public Group getGroupById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Group.class, id);
    }

    @Override
    public void addOrUpdateGroup(Group g) {
        Session s = this.factory.getObject().getCurrentSession();
        if (g.getId() == null) {
            s.persist(g);
        } else {
            s.merge(g);
        }
    }

    @Override
    public void deleteGroup(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Group g = this.getGroupById(id);
        s.remove(g);
    }
    
}
