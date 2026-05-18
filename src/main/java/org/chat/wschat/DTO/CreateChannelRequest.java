package org.chat.wschat.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.chat.wschat.model.Channel;

@Data
public class CreateChannelRequest {

    @NotBlank
    private String name;

    @NotNull
    private Channel.ChannelType type;
}
