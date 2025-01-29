package com.Catania.mySongSetBackend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "song")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private int songId;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "duration")
    private LocalTime duration;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "tabs_url")
    private String tabsUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "is_manual")
    private boolean isManual;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Song other = (Song) obj;

        return Objects.equals(this.title, other.title) &&
                Objects.equals(this.artist, other.artist) &&
                Objects.equals(this.album, other.album) &&
                this.duration == other.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, album, duration);
    }

    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "song_instruments",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id")
    )
    private Set<Instrument> instruments = new HashSet<>();

    @ManyToMany(mappedBy = "songsPlaylist", fetch = FetchType.LAZY)
    private List<Playlist> playlists;

}
