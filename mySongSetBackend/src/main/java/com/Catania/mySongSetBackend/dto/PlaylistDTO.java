package com.Catania.mySongSetBackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlaylistDTO {
    private int id;
    private String name;
    private String description;
    private List<Integer> songIds;

}