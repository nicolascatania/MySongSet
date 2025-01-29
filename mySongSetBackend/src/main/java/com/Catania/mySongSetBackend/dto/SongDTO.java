package com.Catania.mySongSetBackend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Time;
import java.util.Set;

@Data
public class SongDTO {

    private int songId;

    private String title;
    private String artist;
    private String album;
    private Time duration;
    private String imageUrl;
    private String tabsUrl;
    private String videoUrl;

    @JsonProperty("manual")
    private boolean isManual;

    private Set<InstrumentDTO> instruments;
}