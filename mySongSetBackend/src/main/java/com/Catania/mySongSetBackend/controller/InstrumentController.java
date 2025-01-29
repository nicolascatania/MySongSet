package com.Catania.mySongSetBackend.controller;

import com.Catania.mySongSetBackend.dto.InstrumentDTO;
import com.Catania.mySongSetBackend.service.InstrumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;


    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping
    public ResponseEntity<List<InstrumentDTO>> getAllInstruments() {
        List<InstrumentDTO> instruments = instrumentService.getAllInstruments();
        if(instruments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(instruments);
    }

    @GetMapping("/songInstruments")
    public ResponseEntity<Set<InstrumentDTO>> getAllSongInstruments(@RequestBody Set<Integer> instrumentsId) {
        Set<InstrumentDTO> instruments = instrumentService.getSongInstruments(instrumentsId);

        if(instruments.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(instruments);
    }


}
