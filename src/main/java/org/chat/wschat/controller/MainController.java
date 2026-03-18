package org.chat.wschat.controller;

import org.chat.wschat.model.Chat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Chat sendMessage(@Payload Chat message){
        return Chat.ofChat(message.getSender(), message.getContent());
    }

    public Chat addUser(@Payload Chat message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username",message.getSender());
        return Chat.ofJoin(message.getSender());
    }
}
