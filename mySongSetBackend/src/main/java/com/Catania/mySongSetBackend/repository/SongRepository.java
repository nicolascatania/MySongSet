package com.Catania.mySongSetBackend.repository;

import com.Catania.mySongSetBackend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    Optional<Song> findByTitleAndArtistAndAlbum(String title, String artist, String album);
}
