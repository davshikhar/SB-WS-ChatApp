package org.chat.wschat.controller;

import jakarta.validation.Valid;
import org.chat.wschat.DTO.CreateChannelRequest;
import org.chat.wschat.Repository.ChannelRepository;
import org.chat.wschat.Service.ChannelService;
import org.chat.wschat.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<Channel>> getChannels(Principal principal){
        return ResponseEntity.ok(channelService.getAccessibleChannels(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<Channel> createNewChannel(@Valid @RequestBody CreateChannelRequest req, Principal principal){
        Channel channel = channelService.createChannel(req.getName(), req.getType(),
                principal.getName());
        return ResponseEntity.ok(channel);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Channel> joinPublicChannel(@PathVariable String id, Principal principal){
        return ResponseEntity.ok(channelService.joinChannel(id, principal.getName()));
    }

    @PostMapping("/{id}/invite/{username}")
    public ResponseEntity<?> inviteToChannel(@PathVariable String id, @PathVariable String username, Principal principal){
        channelService.inviteToChannel(id, principal.getName(), username);
        return ResponseEntity.ok(Map.of("message","Invite send to the user"));
    }

    @PostMapping("/invites/{inviteId}/respond")
    public ResponseEntity<?> respondToInvite(@PathVariable String inviteId, @RequestParam boolean accept, Principal principal){
        channelService.respondToInvite(inviteId, principal.getName(), accept);
        return ResponseEntity.ok(Map.of("message",accept?"Invite accepted":"Invite Rejected"));
    }

}
