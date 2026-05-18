package org.chat.wschat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="invitees")
public class Invite {

    public enum Status{
        PENDING, ACCEPTED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String channelId;
    private String channelName;
    private String fromUser;
    private String toUser;

    @Enumerated(EnumType.STRING)
    private Status status;
}
