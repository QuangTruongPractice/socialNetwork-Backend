/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.User;
import com.tqt.repositories.UserRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    private static final int PAGE_SIZE = 6;

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root root = q.from(User.class);
        q.select(root);

        if (params != null) {
            // Loc du lieu
            List<Predicate> predcates = new ArrayList<>();

            String userCode = params.get("userCode");
            if (userCode != null && !userCode.isEmpty()) {
                predcates.add(b.like(root.get("userCode"), "%" + userCode + "%"));
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
    public User getUserById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public void addOrUpdateUser(User user) {
        Session s = this.factory.getObject().getCurrentSession();
        if (user.getId() == null) {
            s.persist(user);
        } else {
            s.merge(user);
        }
    }
//    
//    @Override
//    public void deleteUser(int id) {
//        Session s = this.factory.getObject().getCurrentSession();
//        User p = this.getUserById(id);
//        s.remove(p);
//    }

    @Override
    public List<User> findUsersWithoutAccount() {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT u FROM User u WHERE u.id NOT IN (SELECT a.user.id FROM Account a)";
        return s.createQuery(hql, User.class).getResultList();
    }

}
