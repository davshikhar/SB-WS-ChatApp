package org.chat.wschat.Security;

import lombok.RequiredArgsConstructor;
import org.chat.wschat.Repository.ChannelRepository;
import org.chat.wschat.model.Channel;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class SubscriptionInterceptor implements ChannelInterceptor {

    @Autowired
    private ChannelRepository channelRepository;

    private static final String CHANNEL_PREFIX = "/topic/channel.";
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor!=null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            String destination = accessor.getDestination();
            Principal user = accessor.getUser();
            if(destination!=null && destination.startsWith(CHANNEL_PREFIX)){
                String channelId = destination.substring(CHANNEL_PREFIX.length());
                validateAccess(channelId, user);
            }
        }
        return message;
    }

    private void validateAccess(String channelId, Principal user){
        if(user == null)
            throw new IllegalArgumentException(("User not found"));
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if(channelOptional.isEmpty()){
            throw new IllegalArgumentException("channel not found:- "+channelId);
        }

        Channel ch = channelOptional.get();
        if(ch.getType().equals(Channel.ChannelType.PRIVATE)){
            String username = user.getName();
            if(!ch.getMembers().contains(username)){
                throw new IllegalArgumentException(("User is not a member of private channel"));
            }
        }
    }
}
