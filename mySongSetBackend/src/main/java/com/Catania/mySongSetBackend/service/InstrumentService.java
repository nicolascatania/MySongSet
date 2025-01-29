package com.Catania.mySongSetBackend.service;

import com.Catania.mySongSetBackend.dto.InstrumentDTO;
import com.Catania.mySongSetBackend.model.Instrument;
import com.Catania.mySongSetBackend.repository.InstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;

    public InstrumentService(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }


    public List<InstrumentDTO> getAllInstruments() {
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Set<InstrumentDTO> getSongInstruments(Set<Integer> instrumentsId) {
        Set<InstrumentDTO> instruments = new HashSet<>();

        for (Integer instrumentId : instrumentsId) {
            instrumentRepository.findById(instrumentId).ifPresent(instrument -> instruments.add(mapToDTO(instrument)));
        }

        return instruments;
    }



    private InstrumentDTO mapToDTO(Instrument instrument) {
        return new InstrumentDTO(instrument.getId(), instrument.getName());
    }

}
