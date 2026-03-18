package org.chat.wschat.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WSconfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}

/**
This class is the central configuration for entire Websocket system.It sets up the rules for how the messages flow between cliet and server.
 WebSocketMessageBrokerConfigurer is an interface that gives method to customize websocket setup.
 - The broker's job is to deliver messages to all subscribers of a topic
 - "Simple" means it's in-memory — for production you'd use RabbitMQ/ActiveMQ
 client subscribes to /topic/public ---> someone sends message to /topic/public ---> SimpleBroker delivers it to All subscribers.

 setApplicationDestinationPrefixes("/app")
 Any message sent to `/app/...` is routed to your **`@MessageMapping` controller methods**
 It's the prefix that separates "go to my controller" from "go to the broker"

 /app/chat.sendMessage  →  goes to @MessageMapping("/chat.sendMessage") in your controller
 /topic/public          →  goes directly to the broker (for subscriptions)

 this is how visual routing happens:-
 Client sends to /app/hello   →   Your @MessageMapping controller
 Client sends to /topic/public →   SimpleBroker (direct, no controller)
 Client subscribes to /topic/public →  SimpleBroker tracks it


 registerStompEndpoints() :- this method tells where the clients connect to establish a websocket connection.
addEndpoint("/ws-chat"):-  this is the url- client connects here, this is the entry point for all websocket connections.
    client connects here first.

 setAllowedOriginPatterns("*")
 CORS setting — "*" means any domain can connect
 In production you'd restrict this:- it can be something like:-  setAllowedOriginPatterns("*")

 .withSockJS()
 Enables **SockJS fallback** if WebSocket is not available
 Tries WebSocket first, falls back to HTTP polling etc.
 *
 */

