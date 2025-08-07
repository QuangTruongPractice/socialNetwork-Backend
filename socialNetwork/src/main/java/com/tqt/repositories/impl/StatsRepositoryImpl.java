/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Post;
import com.tqt.pojo.User;
import com.tqt.repositories.StatsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
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
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> getUserStatsByYear() {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<User> r = q.from(User.class);

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        q.multiselect(yearExp, b.count(r.get("id")))
                .groupBy(yearExp)
                .orderBy(b.asc(yearExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getUserStatsByYear(int year) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<User> r = q.from(User.class);

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        Expression<Integer> monthExp = b.function("MONTH", Integer.class, r.get("createdAt"));
        q.multiselect(monthExp, b.count(r.get("id")))
                .where(b.equal(yearExp, year))
                .groupBy(monthExp)
                .orderBy(b.asc(monthExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getUserStatsByQuarter(int year, int quarter) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<User> r = q.from(User.class);

        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = startMonth + 2;

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        Expression<Integer> monthExp = b.function("MONTH", Integer.class, r.get("createdAt"));
        q.multiselect(monthExp, b.count(r.get("id")))
                .where(
                        b.equal(yearExp, year),
                        b.between(monthExp, startMonth, endMonth)
                )
                .groupBy(monthExp)
                .orderBy(b.asc(monthExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getUserStatsByMonth(int year, int month) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<User> r = q.from(User.class);

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        Expression<Integer> monthExp = b.function("MONTH", Integer.class, r.get("createdAt"));
        Expression<Integer> dayExp = b.function("DAY", Integer.class, r.get("createdAt"));
        q.multiselect(dayExp, b.count(r.get("id")))
                .where(
                        b.equal(yearExp, year),
                        b.equal(monthExp, month)
                )
                .groupBy(dayExp)
                .orderBy(b.asc(dayExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getPostStatsByYear() {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Post> r = q.from(Post.class);

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        q.multiselect(yearExp, b.count(r.get("id")))
                .groupBy(yearExp)
                .orderBy(b.asc(yearExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getPostStatsByYear(int year) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Post> r = q.from(Post.class);

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        Expression<Integer> monthExp = b.function("MONTH", Integer.class, r.get("createdAt"));
        q.multiselect(monthExp, b.count(r.get("id")))
                .where(b.equal(yearExp, year))
                .groupBy(monthExp)
                .orderBy(b.asc(monthExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getPostStatsByQuarter(int year, int quarter) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Post> r = q.from(Post.class);

        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = startMonth + 2;

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        Expression<Integer> monthExp = b.function("MONTH", Integer.class, r.get("createdAt"));
        q.multiselect(monthExp, b.count(r.get("id")))
                .where(
                        b.equal(yearExp, year),
                        b.between(monthExp, startMonth, endMonth)
                )
                .groupBy(monthExp)
                .orderBy(b.asc(monthExp));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getPostStatsByMonth(int year, int month) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Post> r = q.from(Post.class);

        Expression<Integer> yearExp = b.function("YEAR", Integer.class, r.get("createdAt"));
        Expression<Integer> monthExp = b.function("MONTH", Integer.class, r.get("createdAt"));
        Expression<Integer> dayExp = b.function("DAY", Integer.class, r.get("createdAt"));
        q.multiselect(dayExp, b.count(r.get("id")))
                .where(
                        b.equal(yearExp, year),
                        b.equal(monthExp, month)
                )
                .groupBy(dayExp)
                .orderBy(b.asc(dayExp));

        return s.createQuery(q).getResultList();
    }
}
