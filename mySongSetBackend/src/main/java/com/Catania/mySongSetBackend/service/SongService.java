package com.Catania.mySongSetBackend.service;

import com.Catania.mySongSetBackend.dto.InstrumentDTO;
import com.Catania.mySongSetBackend.dto.SongDTO;
import com.Catania.mySongSetBackend.exceptions.DuplicateSongException;
import com.Catania.mySongSetBackend.model.Instrument;
import com.Catania.mySongSetBackend.model.Song;
import com.Catania.mySongSetBackend.repository.InstrumentRepository;
import com.Catania.mySongSetBackend.repository.SongRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@Service
public class SongService {

    private final SongRepository songRepository;
    private final InstrumentRepository instrumentRepository;

    private static final Logger log = LoggerFactory.getLogger(SongService.class);

    public SongService(SongRepository songRepository, InstrumentRepository instrumentRepository) {
        this.songRepository = songRepository;
        this.instrumentRepository = instrumentRepository;
    }

    @Transactional
    public SongDTO saveSong(SongDTO songDTO) {
        //This method allows to save a song entity related to some instruments
        //when created, it returns the songDTO it response for better data management
        // and if in case that the song already exits, it throws an RuntimeException with an indicator message
        Song song = mapToEntity(songDTO);

        Optional<Song> existingSong = songRepository.findByTitleAndArtistAndAlbum(
                song.getTitle(),
                song.getArtist(),
                song.getAlbum()
        );

        if (existingSong.isPresent()) {
            throw new DuplicateSongException(
                    "Song with title '" + song.getTitle() +
                            "', by '" + song.getArtist() +
                            "' and album '" + song.getAlbum() + "' already exists."
            );
        }
        //USING DTO TO AVOID CYCLING THROUGH RELATIONSHIPS BETWEEN SONG AND INSTRUMENT.
        Song savedSong = songRepository.save(song);
        return mapToDTO(savedSong);
    }
    public List<SongDTO> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(toList());
    }

    public boolean deleteSong(int id) {
        try {
            Song song = songRepository.findById(id).orElse(null);

            if (song != null) {
                songRepository.delete(song);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            log.error("Database error when trying to delete song with id " + id, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when trying to delete song with id " + id, e);
            return false;
        }
    }


    public Song mapToEntity(SongDTO dto) {
        Song song = new Song();
        song.setTitle(dto.getTitle());
        song.setArtist(dto.getArtist());
        song.setAlbum(dto.getAlbum());
        song.setDuration(dto.getDuration().toLocalTime());
        song.setImageUrl(dto.getImageUrl());
        song.setTabsUrl(dto.getTabsUrl());
        song.setVideoUrl(dto.getVideoUrl());
        song.setManual(dto.isManual());


        if(dto.getInstruments() == null){
            Set<Instrument> instruments = new HashSet<>();
        }
        else {
            Set<Integer> instrumentIds = dto.getInstruments().stream()
                    .map(InstrumentDTO::getId)
                    .collect(Collectors.toSet());

            List<Instrument> selectedInstruments = instrumentRepository.findAllById(instrumentIds);
            song.setInstruments(new HashSet<>(selectedInstruments));
        }

        return song;
    }

    public SongDTO mapToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setSongId(song.getSongId());
        dto.setTitle(song.getTitle());
        dto.setArtist(song.getArtist());
        dto.setAlbum(song.getAlbum());

        dto.setDuration(Time.valueOf(song.getDuration()));

        dto.setImageUrl(song.getImageUrl());
        dto.setTabsUrl(song.getTabsUrl());
        dto.setVideoUrl(song.getVideoUrl());
        dto.setManual(song.isManual());

        dto.setInstruments(song.getInstruments().stream()
                .map(instrument -> new InstrumentDTO(instrument.getId(), instrument.getName()))
                .collect(Collectors.toSet()));


        return dto;
    }

}