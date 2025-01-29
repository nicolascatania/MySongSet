package com.Catania.mySongSetBackend.dto;


import lombok.Data;

@Data
public class InstrumentDTO {
    private int id;
    private String name;

    public InstrumentDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
