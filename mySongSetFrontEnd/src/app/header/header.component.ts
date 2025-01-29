import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Song } from '../../models/Song';
import { FormsModule } from '@angular/forms';
import { Playlist } from '../../models/Playlist';
import { PlaylistServiceService } from '../services/playlist-service.service';
import { SongServiceService } from '../services/song-service.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  isSpotifySearchModalOpen = false;
  isPlaylistMenuVisible: { [songId: number]: boolean } = {};
  searchQuery = '';
  searchResults: Song[] = [];
  playlists: Playlist[] = [];
  selectedPlaylist: Playlist | null = null;

  constructor(private http: HttpClient, private playlistService: PlaylistServiceService, private songService: SongServiceService) {}



  openSpotifySearchModal() {
    this.isSpotifySearchModalOpen = true;
  }

  closeSpotifySearchModal() {
    this.closeModalsAndReset();
  }

  onSearch() {
    if (this.searchQuery.trim()) {
      this.http.get<any[]>(`http://localhost:8080/spotify/search-tracks?query=${encodeURIComponent(this.searchQuery)}`).subscribe(
        (response) => {
          this.searchResults = response;
  
          this.searchResults.forEach((song, index) => {
            song.songId = index + 1; //I have to asign id manually for the @for directive on the html file, spotify provides a String id, while I use a numeric one
            //this is the first solution i thougth of, It was a desing error, I did not planned to consume Spotify API from the begining 
          });
        },
        (error) => {
          console.error('Error searching for songs:', error);
        }
      );
    } else {
      this.searchResults = [];
    }
  }
  


  showPlaylistMenu(songId: number) {
    this.isPlaylistMenuVisible[songId] = !this.isPlaylistMenuVisible[songId];
    this.playlistService.getPlaylists().subscribe(
      playlistsResponse =>{
        this.playlists = playlistsResponse;
      },
      error =>{
        console.log(error);
      }
    );
  }
  
  addToPlaylist(playlistId: number, song: Song) {
    if (playlistId === -1) {
      this.songService.saveSong(song).subscribe(
        response => {
          alert("Song saved successfully to default playlist");
        },
        error => {
          alert("Error: could not save the song to the default playlist");
          console.log(error);
        }
      );
    } else {
      let songs: Song[] | null = [];
      
      this.songService.getAllSongs().subscribe(
        res => {
          songs = res;
          const existingSong = songs?.find(s => s.title === song.title && s.artist === song.artist);
      
          if (existingSong) {
            this.addSongToSpecificPlaylist(playlistId, existingSong.songId);
          } else {
            this.songService.saveSong(song).subscribe(
              songResponse => {
                this.addSongToSpecificPlaylist(playlistId, songResponse.songId);
              },
              saveError => {
                alert("Error: could not save the song to the database");
                console.error("Error saving the song in default playlist: ", saveError);
              }
            );
          }
        },
        error => {
          alert("Error: could not retrieve songs from the default playlist");
          console.error("Error fetching all songs: ", error);
        }
      );
      this.playlistService.updatePlaylists(); 
      this.isPlaylistMenuVisible = {};  
    }
    
  }
  
  
  private addSongToSpecificPlaylist(playlistId: number, songId: number) {
    this.playlistService.addSongsToPlaylist(playlistId, [songId]).subscribe(
      updatedPlaylist => {
        alert("Song added successfully to the playlist");
      },
      error => {
        alert("Error: could not add the song to the playlist");
        console.error("Error adding song to playlist: ", error);
      }
    );
  }
  
  private closeModalsAndReset() {
    this.isSpotifySearchModalOpen = false;
    this.searchQuery = '';        
    this.searchResults = [];      
    this.isSpotifySearchModalOpen = false;  
    this.isPlaylistMenuVisible = {};      
  }


  
  

}