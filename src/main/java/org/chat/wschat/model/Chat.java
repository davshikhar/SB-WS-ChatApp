package org.chat.wschat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    public enum Type{
        JOIN, //for joining the chat room
        CHAT, //for sending messaged
        LEAVE //for leaving the room
    }
    private Type type;
    private String content;
    private String sender;
    private String timeStamp;
    private String avatar; //initial avatar photo

    public static Chat ofChat(String sender, String content){
        Chat msg = new Chat();
        msg.setType(Type.CHAT);
        msg.setSender(sender);
        msg.setContent(content);
        msg.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        msg.setAvatar(getInitials(sender));
        return msg;
    }

    public static Chat ofJoin(String sender){
        Chat msg = new Chat();
        msg.setType(Type.JOIN);
        msg.setSender(sender);
        msg.setContent(sender+" joined");
        msg.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        msg.setAvatar(getInitials(sender));
        return msg;
    }

    public static Chat ofLeave(String sender){
        Chat msg = new Chat();
        msg.setType(Type.LEAVE);
        msg.setSender(sender);
        msg.setContent(sender+" left");
        msg.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        msg.setAvatar(getInitials(sender));
        return msg;
    }

    private static String getInitials(String sender){
        if(sender == null || sender.isBlank()){
            return "?";
        }
        String []parts = sender.trim().split("\\s+");
        if(parts.length>=2){
            return (""+parts[0].charAt(0)+parts[1].charAt(0)).toUpperCase();
        }
        return sender.substring(0,Math.min(2,sender.length())).toUpperCase();
    }
}
