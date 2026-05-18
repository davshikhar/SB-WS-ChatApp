package org.chat.wschat.component;

import lombok.extern.slf4j.Slf4j;
import org.chat.wschat.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 1.We need this EventListener because when the user click the disconnect button in that approach
 we can use the controller but in case the phone dies, or Wi-Fi drops or internet disconnects.
 2. in case the user closes the browser, the browser automatically send a close frame
 3.Spring's websocket layer receives it ----> Fired the SessionDisconnectEvent ----> EventListener picks it up --> code runs and leaves the message.

 4.Spring also send periodic pings to detects dead connections.
 *
 */
@Component
@Slf4j
public class WSEventListener {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String channelId = (String) headerAccessor.getSessionAttributes().get("channelId");

        if(username != null && channelId != null){
            log.info("User="+username+" disconnected from channel="+channelId);
            Chat chat = Chat.builder().type(Chat.Type.LEAVE).sender(username).channelId(channelId).content(username+" left the channel")
                    .build();
            messageSendingOperations.convertAndSend("/topic/channel."+channelId, chat);
        }
    }
}
