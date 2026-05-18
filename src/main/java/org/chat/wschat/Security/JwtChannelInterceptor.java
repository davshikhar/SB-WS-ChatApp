package org.chat.wschat.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel){
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())){
            String token = extractToken(accessor);
            if(!StringUtils.hasText(token) || !jwtUtil.validateToken(token)){
                throw new IllegalArgumentException("Invalid or missing token!");
            }

            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            accessor.setUser(auth);

            // Store username in session for subsequent messages
            accessor.getSessionAttributes().put("username", username);
        } else if(accessor != null && accessor.getUser() == null) {
            // For non-CONNECT messages, restore user from session
            String username = (String) accessor.getSessionAttributes().get("username");
            if(username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                accessor.setUser(auth);
            }
        }

        return message;
    }

    public String extractToken(StompHeaderAccessor accessor){
        //creating this because this works with stomp frame headers
        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return accessor.getFirstNativeHeader("token");
    }
}
