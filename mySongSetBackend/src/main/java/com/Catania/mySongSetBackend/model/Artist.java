package com.Catania.mySongSetBackend.model;

import lombok.Data;

@Data
public class Artist {
    private String id;
    private String name;
    private int popularity;
    private String[] genres;
}