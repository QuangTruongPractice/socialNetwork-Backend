/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Account;
import com.tqt.repositories.AccountRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Quang Truong
 */
@Repository
@Transactional
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    private static final int PAGE_SIZE = 10;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAccounts(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Account> q = b.createQuery(Account.class);
        Root root = q.from(Account.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predcates = new ArrayList<>();

            String email = params.get("email");
            if (email != null && !email.isEmpty()) {
                predcates.add(b.like(root.get("email"), "%" + email + "%"));
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
        String hql = "SELECT COUNT(a) FROM Account a";
        Query query = s.createQuery(hql, Long.class);
        Long count = (Long)query.getSingleResult();
        return (int) Math.ceil(count * 1.0 / PAGE_SIZE);
    }

    @Override
    public void addOrUpdateAccount(Account account) {
        Session s = this.factory.getObject().getCurrentSession();
        if (account.getId() == null) {
            s.persist(account);
        } else {
            s.merge(account);
        }
    }

    @Override
    public Account getAccountById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Account.class, id);
    }

    @Override
    public Account getAccountByUserId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Account WHERE user.id = :userId";
        return session.createQuery(hql, Account.class)
                .setParameter("userId", id)
                .uniqueResult();
    }

    @Override
    public List<Account> getAccountByGroupId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT a FROM Account a "
                + "JOIN a.user u "
                + "JOIN u.groups g "
                + "WHERE g.id = :groupId";
        return session.createQuery(hql, Account.class)
                .setParameter("groupId", id)
                .getResultList();
    }

    @Override
    public Account getAccountByEmail(String email) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Account a WHERE a.email = :email";
        return session.createQuery(hql, Account.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public List<Account> getPendingAccounts() {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Account a WHERE a.isVerified = false";
        return session.createQuery(hql, Account.class)
                .getResultList();
    }

    @Override
    public boolean existAccountByEmail(String email) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(a) FROM Account a WHERE a.email = :email";
        Long count = session.createQuery(hql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean authenticate(String email, String password) {
        Account a = this.getAccountByEmail(email);
        if (a == null) {
            return false;
        }

        return this.passwordEncoder.matches(password, a.getPassword());
    }

}
