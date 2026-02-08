package com.tqt.repositories.impl;

import com.tqt.pojo.ChatMessage;
import com.tqt.repositories.ChatRepository;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ChatRepositoryImpl implements ChatRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ChatMessage addMessage(ChatMessage message) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(message);
        return message;
    }

    @Override
    public List<ChatMessage> getChatHistory(Integer user1, Integer user2) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT c FROM ChatMessage c WHERE (c.sender.id = :u1 AND c.recipient.id = :u2) "
                + "OR (c.sender.id = :u2 AND c.recipient.id = :u1) ORDER BY c.timestamp ASC";
        return s.createQuery(hql, ChatMessage.class)
                .setParameter("u1", user1)
                .setParameter("u2", user2)
                .getResultList();
    }

    @Override
    public ChatMessage getLastMessage(Integer user1, Integer user2) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT c FROM ChatMessage c WHERE (c.sender.id = :u1 AND c.recipient.id = :u2) "
                + "OR (c.sender.id = :u2 AND c.recipient.id = :u1) ORDER BY c.timestamp DESC";
        List<ChatMessage> results = s.createQuery(hql, ChatMessage.class)
                .setParameter("u1", user1)
                .setParameter("u2", user2)
                .setMaxResults(1)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Integer> getRecentChatPartners(Integer userId) {
        Session s = this.factory.getObject().getCurrentSession();
        // Get all other users who sent messages to this user
        String hql1 = "SELECT DISTINCT c.sender.id FROM ChatMessage c WHERE c.recipient.id = :uid";
        List<Integer> partners = s.createQuery(hql1, Integer.class)
                .setParameter("uid", userId)
                .getResultList();

        // Get all other users who received messages from this user
        String hql2 = "SELECT DISTINCT c.recipient.id FROM ChatMessage c WHERE c.sender.id = :uid";
        List<Integer> recipientIds = s.createQuery(hql2, Integer.class)
                .setParameter("uid", userId)
                .getResultList();

        // Combine and find distinct IDs
        for (Integer id : recipientIds) {
            if (!partners.contains(id)) {
                partners.add(id);
            }
        }
        return partners;
    }
}
