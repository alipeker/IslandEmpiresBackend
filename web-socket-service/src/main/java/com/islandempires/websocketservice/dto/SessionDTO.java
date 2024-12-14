package com.islandempires.websocketservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.islandempires.websocketservice.model.IslandResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDTO {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("jwtToken")
    private String jwtToken;

    @JsonProperty("serverID")
    private String serverID;

    @JsonProperty("islandResourceList")
    private List<Object> islandResourceList;

}
