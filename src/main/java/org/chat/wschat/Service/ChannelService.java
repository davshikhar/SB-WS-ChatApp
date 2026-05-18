package org.chat.wschat.Service;

import org.chat.wschat.Repository.ChannelRepository;
import org.chat.wschat.Repository.UserRepository;
import org.chat.wschat.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Channel createChannel(String name, Channel.ChannelType type, String creatorUsername){
        if(channelRepository.existsByName(name)){
            throw new IllegalArgumentException("Channel already exists!");
        }
        Set<String> members = new HashSet<>();
        members.add(creatorUsername); //adding creator since creator is always a member

        Channel channel = Channel.builder()
                .name(name)
                .type(type)
                .createdBy(creatorUsername)
                .members(members)
                .build();

        return channelRepository.save(channel);
    }

    public List<Channel> getAccessibleChannels(String username){
        //finding the channels that can be accessed by the specific user
        return channelRepository.findAccessibleChannels(username);
    }

    @Transactional
    public Channel joinChannel(String channelId, String username){
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new IllegalArgumentException(("Channel not found:- ")));

        if (channel.getType() == Channel.ChannelType.PRIVATE) {
            throw new IllegalStateException("Cannot self-join a private channel; must be invited");
        }

        channel.getMembers().add(username);
        return channelRepository.save(channel);
    }

    @Transactional
    public Channel inviteToChannel(String channelId, String inviterUsername, String targetUsername) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new IllegalArgumentException(("Channel not found:- ")));

        if (!channel.getMembers().contains(inviterUsername)) {
            throw new IllegalStateException("Inviter is not a member of the channel");
        }

        if (!userRepository.existsByUsername(targetUsername)) {
            throw new IllegalArgumentException("Target user not found: " + targetUsername);
        }

        if(channel.getType() == Channel.ChannelType.PUBLIC)
            throw new IllegalArgumentException("Use join for public channels");

        channel.getMembers().add(targetUsername);
        return channelRepository.save(channel);
    }

    public boolean isMember(String channelId, String username){
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new IllegalArgumentException(("Channel not found!")));
        if(channel.getType() == Channel.ChannelType.PUBLIC)
            return true;
        return channel.getMembers().contains(username);
    }

    public Channel getChannelByName(String name){
        return channelRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("Channel not found: " + name));
    }

    public boolean isMemberByName(String channelName, String username){
        Channel channel = channelRepository.findByName(channelName).orElseThrow(() -> new IllegalArgumentException("Channel not found: " + channelName));
        if(channel.getType() == Channel.ChannelType.PUBLIC)
            return true;
        return channel.getMembers().contains(username);
    }
}
