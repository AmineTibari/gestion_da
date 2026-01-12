package com.example.gestion_users.Controller;

import com.example.gestion_users.Entity.Message;
import com.example.gestion_users.Service.facade.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String subject = payload.get("subject").toString();
        String content = payload.get("content").toString();

        return messageService.sendMessage(senderId, receiverId, subject, content);
    }

    @GetMapping("/received/{userId}")
    public List<Message> getReceivedMessages(@PathVariable Long userId) {
        return messageService.getReceivedMessages(userId);
    }

    @GetMapping("/sent/{userId}")
    public List<Message> getSentMessages(@PathVariable Long userId) {
        return messageService.getSentMessages(userId);
    }

    @PutMapping("/read/{id}")
    public Message markAsRead(@PathVariable Long id) {
        return messageService.markAsRead(id);
    }

    @GetMapping("/unread-count/{userId}")
    public Map<String, Integer> getUnreadCount(@PathVariable Long userId) {
        return Map.of("count", messageService.getUnreadCount(userId));
    }
}
