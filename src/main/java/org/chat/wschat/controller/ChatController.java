package org.chat.wschat.controller;

import org.chat.wschat.Service.ChannelService;
import org.chat.wschat.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @Autowired
    private ChannelService channelService;

    @MessageMapping("/channel.{channelName}.send")
    public void sendMessage(@DestinationVariable String channelName,
                            @Payload Chat message,
                            Principal principal){
        if(!channelService.isMemberByName(channelName, principal.getName())){
            throw new IllegalArgumentException("You are not a member of this channel!");
        }
        message.setSender(principal.getName());
        message.setChannelId(channelName);
        message.setType(Chat.Type.CHAT);
        messageSendingOperations.convertAndSend("/topic/channel."+channelName, message);
    }

    @MessageMapping("/channel.{channelName}.join")
    public void joinChannel(@DestinationVariable String channelName,
                            @Payload Chat message,
                            SimpMessageHeaderAccessor simpMessageHeaderAccessor,
                            Principal principal){
        simpMessageHeaderAccessor.getSessionAttributes().put("username",principal.getName());
        simpMessageHeaderAccessor.getSessionAttributes().put("channelName",channelName);

        message.setSender(principal.getName());
        message.setChannelId(channelName);
        message.setType(Chat.Type.JOIN);
        messageSendingOperations.convertAndSend("/topic/channel."+channelName, message);
    }
}
