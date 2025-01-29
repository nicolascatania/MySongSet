package com.Catania.mySongSetBackend.repository;

import com.Catania.mySongSetBackend.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    @Query("SELECT p FROM Playlist p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Playlist> findByNameIgnoreCase(@Param("name") String name);

}
