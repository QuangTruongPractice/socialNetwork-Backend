/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.repositories.impl;

import com.tqt.pojo.Comment;
import com.tqt.repositories.CommentRepository;
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
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Comment> getCommentByPostId(int postId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM Comment c WHERE c.post.id = :postId AND c.isDeleted = false ORDER BY c.createdDate DESC";

        return session.createQuery(hql, Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    @Override
    public Comment getCommentById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Comment.class, id);
    }

    @Override
    public Comment addComment(Comment c) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(c);
        return c;
    }

    @Override
    public Comment updateComment(Comment c) {
        Session s = this.factory.getObject().getCurrentSession();
        return (Comment) s.merge(c);
    }

}
