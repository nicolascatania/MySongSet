import { Component, OnInit } from '@angular/core';
import { PlaylistServiceService } from '../services/playlist-service.service';
import { Playlist } from '../../models/Playlist';
import { CardModalComponent } from "../card-modal/card-modal.component";
import { Song } from '../../models/Song';
import { SongServiceService } from '../services/song-service.service';

@Component({
  selector: 'app-content-display',
  templateUrl: './content-display.component.html',
  styleUrls: ['./content-display.component.css'],
  standalone: true,
  imports: [CardModalComponent]
})
export class ContentDisplayComponent implements OnInit {
  selectedPlaylist: Playlist | null = null;
  selectedSong: Song | null = null;
  totalDuration: string = "";

  constructor(
    private playlistService: PlaylistServiceService,
    private songService: SongServiceService
  ) {}

  ngOnInit(): void {
    this.playlistService.selectedPlaylist$.subscribe((playlist) => {
      this.selectedPlaylist = playlist;
      if (playlist) {
        this.totalDuration = this.playlistService.calculateTotalDuration(
          playlist.songs
        );
      }
    });
  }

  openModal(song: Song): void {
    this.selectedSong = song;
  }

  closeModal(): void {
    this.selectedSong = null;
  }

  deleteSong(song: Song): void {
    if (this.selectedPlaylist && this.selectedPlaylist.id !== -1) {
      this.playlistService
        .removeSongFromPlaylistId(this.selectedPlaylist.id, song.songId)
        .subscribe(
          () => {
            if (this.selectedPlaylist) {
              this.selectedPlaylist.songs = this.selectedPlaylist.songs.filter(
                (playlistSong) => playlistSong.songId !== song.songId
              );
              this.totalDuration = this.playlistService.calculateTotalDuration(
                this.selectedPlaylist.songs
              );
            }
          },
          (error) => {
            console.error("Error deleting song from playlist:", error);
          }
        );
    } else {
      this.songService.deleteSong(song.songId).subscribe(
        (response) => {
          alert(response);
          if (this.selectedPlaylist) {
            this.selectedPlaylist.songs = this.selectedPlaylist.songs.filter(
              (playlistSong) => playlistSong.songId !== song.songId
            );
            this.totalDuration = this.playlistService.calculateTotalDuration(
              this.selectedPlaylist.songs
            );
          }
        },
        (error) => {
          console.error("Error deleting song from all songs playlist:", error);
        }
      );
    }
    this.closeModal();
  }
}
