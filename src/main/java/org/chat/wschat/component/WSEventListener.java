package org.chat.wschat.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 1.We need this EventListener because when the user click the disconnect button in that approach
 we can use the controller but in case the phone dies, or wifi drops or internet disconnects.
 *
 */
@Component
public class WSEventListener {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent){

    }
}
