package com.islandempires.clanservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerUserConversationMessageDTO implements Serializable {
    @NotBlank
    private Long conversationId;

    @Size(min = 10, max = 2000)
    private String messageBody;
}
