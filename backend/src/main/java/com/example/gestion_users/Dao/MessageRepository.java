package com.example.gestion_users.Dao;

import com.example.gestion_users.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverIdOrderByDateEnvoiDesc(Long receiverId);

    List<Message> findBySenderIdOrderByDateEnvoiDesc(Long senderId);

    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
}
