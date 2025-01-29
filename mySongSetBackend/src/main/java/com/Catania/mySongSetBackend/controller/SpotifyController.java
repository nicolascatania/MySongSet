package com.Catania.mySongSetBackend.controller;

import com.Catania.mySongSetBackend.dto.SongDTO;
import com.Catania.mySongSetBackend.model.Artist;
import com.Catania.mySongSetBackend.service.SpotifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search")
    public Artist[] searchArtist(@RequestParam String artist) {
        return spotifyService.searchArtist(artist);
    }

    @GetMapping("/top-tracks/{artistId}")
    public ResponseEntity<List<SongDTO>> getTopTracks(@PathVariable String artistId) {
        try {
            List<SongDTO> topTracks = spotifyService.getTopTracks(artistId);
            return ResponseEntity.ok(topTracks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search-tracks")
    public ResponseEntity<List<SongDTO>> searchTracks(@RequestParam String query) {
        try {
            List<SongDTO> tracks = spotifyService.searchTracks(query);
            return ResponseEntity.ok(tracks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

