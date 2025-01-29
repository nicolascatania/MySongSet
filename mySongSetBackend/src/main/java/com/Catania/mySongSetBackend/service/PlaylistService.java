package com.Catania.mySongSetBackend.service;


import com.Catania.mySongSetBackend.dto.PlaylistDTO;
import com.Catania.mySongSetBackend.dto.SongDTO;
import com.Catania.mySongSetBackend.exceptions.DuplicateEntityException;
import com.Catania.mySongSetBackend.exceptions.DuplicatePlaylistNameException;
import com.Catania.mySongSetBackend.exceptions.PlaylistNotFoundException;
import com.Catania.mySongSetBackend.exceptions.SongNotFoundException;
import com.Catania.mySongSetBackend.model.Playlist;
import com.Catania.mySongSetBackend.model.Song;
import com.Catania.mySongSetBackend.repository.PlaylistRepository;
import com.Catania.mySongSetBackend.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final SongService songService;

    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository, SongService songService) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.songService = songService;
    }

    public PlaylistDTO createPlaylist(PlaylistDTO playlistDTO) {
        playlistRepository.findByNameIgnoreCase(playlistDTO.getName()).ifPresent(existingPlaylist -> {
            throw new DuplicatePlaylistNameException("There is an existing playlist with name: " + playlistDTO.getName());
        });

        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return convertToPlaylistDTO(savedPlaylist);
    }

    public PlaylistDTO addSongsToPlaylist(int playlistId, List<Integer> songIds) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with ID " + playlistId + " was not found"));

        List<Song> songs = songRepository.findAllById(songIds);

        if (songs.isEmpty()) {
            throw new SongNotFoundException("Songs with ID " + songIds + "were not found");
        }

        List<Integer> existingSongIds = playlist.getSongsPlaylist().stream()
                .map(Song::getSongId)
                .toList();

        List<Song> duplicateSongs = songs.stream()
                .filter(song -> existingSongIds.contains(song.getSongId()))
                .toList();

        if (!duplicateSongs.isEmpty()) {
            throw new DuplicateEntityException("The following songs are already in the playlist: " +
                    duplicateSongs.stream().map(Song::getTitle).collect(Collectors.joining(", ")));
        }

        playlist.getSongsPlaylist().addAll(songs);
        Playlist savedPlaylist = playlistRepository.save(playlist);

        return convertToPlaylistDTO(savedPlaylist);
    }

    public void removeSongFromPlaylist(int playlistId, int songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        playlist.getSongsPlaylist().remove(song);
        song.getPlaylists().remove(playlist);

        playlistRepository.save(playlist);
        songRepository.save(song);
    }

    public List<PlaylistDTO> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::convertToPlaylistDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> getSongsInPlaylist(int playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));

        return playlist.getSongsPlaylist().stream()
                .map(songService::mapToDTO)
                .collect(Collectors.toList());
    }

    public void removePlaylist(int playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        playlistRepository.delete(playlist);
    }

    public PlaylistDTO convertToPlaylistDTO(Playlist playlist) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setSongIds(
                playlist.getSongsPlaylist() != null
                        ? playlist.getSongsPlaylist().stream().map(Song::getSongId).collect(Collectors.toList())
                        : new ArrayList<>()
        );
        return dto;
    }

    public Playlist convertToPlaylistEntity(PlaylistDTO dto, List<Song> songs) {
        Playlist playlist = new Playlist();
        playlist.setId(dto.getId());
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setSongsPlaylist(songs != null ? songs : new ArrayList<>());
        return playlist;
    }

    public PlaylistDTO updatePlaylist(int id, PlaylistDTO playlistDTO) {
        Optional<Playlist> foundedPlaylist = playlistRepository.findById(id);
        if(foundedPlaylist.isEmpty()) {
            throw new PlaylistNotFoundException("Can not edit this playlist, it does not exist.");
        }
        Playlist playlist = foundedPlaylist.get();
        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());
        playlistRepository.save(playlist);

        return convertToPlaylistDTO(playlist);
    }
}
