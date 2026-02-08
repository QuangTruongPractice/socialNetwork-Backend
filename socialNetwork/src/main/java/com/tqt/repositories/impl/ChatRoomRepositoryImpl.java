package com.tqt.repositories.impl;

import com.tqt.pojo.ChatRoom;
import com.tqt.repositories.ChatRoomRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ChatRoom getChatRoomById(String chatRoomId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT r FROM ChatRoom r WHERE r.chatRoomId = :roomId";
        return s.createQuery(hql, ChatRoom.class)
                .setParameter("roomId", chatRoomId)
                .uniqueResult();
    }

    @Override
    public void addChatRoom(ChatRoom room) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(room);
    }

    @Override
    public List<ChatRoom> getRoomsByUser(Integer userId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT r FROM ChatRoom r WHERE r.user1Id = :uid OR r.user2Id = :uid";
        return s.createQuery(hql, ChatRoom.class)
                .setParameter("uid", userId)
                .getResultList();
    }
}
