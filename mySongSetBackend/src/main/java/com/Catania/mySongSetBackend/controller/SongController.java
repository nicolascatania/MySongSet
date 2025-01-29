package com.Catania.mySongSetBackend.controller;

import com.Catania.mySongSetBackend.dto.SongDTO;
import com.Catania.mySongSetBackend.service.SongService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public ResponseEntity<SongDTO> addSong(@RequestBody SongDTO songDTO) {

        try {
            SongDTO savedSong = songService.saveSong(songDTO);
            return new ResponseEntity<>(savedSong, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<SongDTO> songs = songService.getAllSongs();
        if(songs.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable int id) {
        boolean response = songService.deleteSong(id);
        if (response) {
            return ResponseEntity.ok("Song with id '" + id + "' deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Song with id '" + id + "' not found");
    }

}
