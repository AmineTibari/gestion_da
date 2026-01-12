package com.example.gestion_users.Service.Impl;

import com.example.gestion_users.Dao.MessageRepository;
import com.example.gestion_users.Dao.UtilisateurRepository;
import com.example.gestion_users.Entity.Message;
import com.example.gestion_users.Entity.Utilisateur;
import com.example.gestion_users.Service.facade.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String subject, String content) {
        Utilisateur sender = utilisateurRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Utilisateur receiver = utilisateurRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSubject(subject);
        message.setContent(content);
        message.setDateEnvoi(LocalDateTime.now());
        message.setIsRead(false);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getReceivedMessages(Long userId) {
        return messageRepository.findByReceiverIdOrderByDateEnvoiDesc(userId);
    }

    @Override
    public List<Message> getSentMessages(Long userId) {
        return messageRepository.findBySenderIdOrderByDateEnvoiDesc(userId);
    }

    @Override
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsRead(true);
        return messageRepository.save(message);
    }

    @Override
    public int getUnreadCount(Long userId) {
        return messageRepository.findByReceiverIdAndIsReadFalse(userId).size();
    }
}
