package com.example.gestion_users.Service.facade;

import com.example.gestion_users.Entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(Long senderId, Long receiverId, String subject, String content);

    List<Message> getReceivedMessages(Long userId);

    List<Message> getSentMessages(Long userId);

    Message markAsRead(Long messageId);

    int getUnreadCount(Long userId);
}
