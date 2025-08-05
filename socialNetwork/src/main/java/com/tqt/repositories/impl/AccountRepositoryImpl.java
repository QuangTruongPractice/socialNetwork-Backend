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
import java.time.LocalDateTime;
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
    private static final int PAGE_SIZE = 6;

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
            // Loc du lieu
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
    public void addOrUpdateAccount(Account account) {
        Session s = this.factory.getObject().getCurrentSession();
        if ("LECTURER".equals(account.getRole().name())) {
            account.setMustChangePassword(true);
            account.setPasswordExpiresAt(LocalDateTime.now().plusHours(24));
        } else {
            if (Boolean.TRUE.equals(account.getMustChangePassword())) {
                account.setPasswordExpiresAt(LocalDateTime.now().plusHours(24));
            } else {
                account.setPasswordExpiresAt(null);
            }
        }
        if (account.getId() == null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            s.persist(account);
        } else {
            Account existing = s.get(Account.class, account.getId());
            if (!existing.getPassword().equals(account.getPassword())) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
            }
            s.merge(account);
        }
    }

    @Override
    public Account getAccountById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Account.class, id);
    }

    @Override
    public Account getAccountByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        org.hibernate.query.Query<Account> q = s.createQuery(
                "FROM Account a WHERE a.email = :email", Account.class);
        q.setParameter("email", email);

        List<Account> results = q.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
