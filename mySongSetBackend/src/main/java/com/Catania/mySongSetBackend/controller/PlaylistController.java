package com.Catania.mySongSetBackend.controller;

import com.Catania.mySongSetBackend.dto.PlaylistDTO;
import com.Catania.mySongSetBackend.dto.SongDTO;
import com.Catania.mySongSetBackend.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;


    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;

    }

    @PostMapping("/create")
    public ResponseEntity<PlaylistDTO> createPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        PlaylistDTO createdPlaylist = playlistService.createPlaylist(playlistDTO);
        return ResponseEntity.ok(createdPlaylist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable int id, @RequestBody PlaylistDTO playlistDTO ) {
        PlaylistDTO updatedPlaylist = playlistService.updatePlaylist(id, playlistDTO);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @PostMapping("/{playlistId}/add-songs")
    public ResponseEntity<PlaylistDTO> addSongsToPlaylist(@PathVariable int playlistId, @RequestBody List<Integer> songIds) {
        PlaylistDTO updatedPlaylist = playlistService.addSongsToPlaylist(playlistId, songIds);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(@PathVariable int playlistId, @PathVariable int songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        List<PlaylistDTO> playlists = playlistService.getAllPlaylists();
        if (playlists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<List<SongDTO>> getSongsInPlaylist(@PathVariable int playlistId) {
        List<SongDTO> songs = playlistService.getSongsInPlaylist(playlistId);
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(songs);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable int playlistId) {
        playlistService.removePlaylist(playlistId);
        return ResponseEntity.ok().build();
    }
}
