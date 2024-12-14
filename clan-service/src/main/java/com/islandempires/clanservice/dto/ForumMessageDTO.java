package com.islandempires.clanservice.dto;

import com.islandempires.clanservice.model.ForumContent;
import com.islandempires.clanservice.model.ServerUser;
import com.islandempires.clanservice.model.UrlWrapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumMessageDTO implements Serializable {

    private Long id;

    @Size(max = 2000)
    private String body;

    @Size(max = 10)
    private List<UrlWrapper> attachment = new ArrayList<>();

    private String localDateTime;
}
