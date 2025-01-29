import { Component, inject, OnInit, signal } from '@angular/core';
import { Playlist } from '../../models/Playlist';
import { PlaylistServiceService } from '../services/playlist-service.service';
import { SongServiceService } from '../services/song-service.service';
import { PlaylistDTO } from '../../models/PlaylistDTO';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { PlaylistModalComponent } from "../playlist-modal/playlist-modal.component";
import { last } from 'rxjs';
import { Instrument } from '../../models/Instrument';
import { InstrumentService } from '../services/instrument.service';




@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [ReactiveFormsModule, PlaylistModalComponent],
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css'],
})
export class SideBarComponent implements OnInit {


  selectedPlaylistId: number | null = null;

  playlists: Playlist[] = [];
  selectedPlaylistToEdit?: PlaylistDTO;
  isModalOpen = false;

  constructor(
    private playlistService: PlaylistServiceService,
    private songService: SongServiceService,
    private instrumentService: InstrumentService
  ) {

  }

  ngOnInit(): void {

    this.playlistService.playlists$.subscribe(
      (playlists) => {
        this.playlists = playlists;
      },
      (error) => {
        console.error('Error fetching playlists:', error);
      }
    );
    
    this.loadPlaylists();
    this.loadDefaultPlaylist();
  }


  loadPlaylists(): void {
    this.playlistService.getPlaylists().subscribe(
      (playlists) => {
        this.playlists = playlists;
      },
      (error) => {
        console.error('Error fetching playlists:', error);
      }
    );
  }

  getSongsInPlaylist(playlistId: number): void {
    const playlist = this.playlists.find((p) => p.id === playlistId);

    if (playlist) {

      this.playlistService.setSelectedPlaylist(playlist);

      this.playlistService.getSongsInPlaylist(playlistId).subscribe(
        (songs) => {
          playlist.songs = songs || []; 
          playlist.duration = this.playlistService.calculateTotalDuration(playlist.songs); 
          this.playlistService.setSelectedPlaylist(playlist); 
        },
        (error) => {
          console.error(
            `Error fetching songs for playlist ${playlistId}:`,
            error
          );
        }
      );
    } else {
      console.error(`Playlist with ID ${playlistId} not found locally.`);
    }
  }

  loadDefaultPlaylist(): void {
    const defaultPlaylist: Playlist = {
      id: -1,
      name: 'All Songs',
      description: 'All the songs I know to play.',
      duration: "",
      songs: [],
    };
  
    this.playlistService.setSelectedPlaylist(defaultPlaylist);

    this.songService.getAllSongs().subscribe(
      (songs) => {
        defaultPlaylist.songs = songs || [];
        defaultPlaylist.duration = this.playlistService.calculateTotalDuration(songs); 
        this.playlistService.setSelectedPlaylist(defaultPlaylist); 
      },
      (error) => {
        console.error('Error fetching all the songs available', error);
      }
    );
  }

  createOrUpdatePlaylist(playlistData: PlaylistDTO): void {
    const selectedPlaylistId = this.selectedPlaylistToEdit
      ? this.playlists.find((playlist) => playlist.name === this.selectedPlaylistToEdit?.name)?.id
      : undefined;
  
    if (selectedPlaylistId) {
      this.playlistService.updatePlaylist(selectedPlaylistId, playlistData).subscribe(
        () => {
          this.closeModal();
          this.loadPlaylists();
        },
        (error) => console.error(error)
      );
    } else {
      this.playlistService.createPlaylist(playlistData).subscribe(
        (newPlaylist) => {
          this.playlists.push(newPlaylist);
          this.closeModal();
        },
        (error) => console.error(error)
      );
    }
  }
  

  openModalToCreate(): void {
    this.selectedPlaylistToEdit = undefined;
    this.isModalOpen = true;
  }

  openModalToEdit(playlist: PlaylistDTO): void {
    this.selectedPlaylistToEdit = playlist;
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  markSelectedPlaylist(playlistId: number | null): void {
    this.selectedPlaylistId = playlistId;
  }

  deletePlaylist(playlistId: number) {
    this.playlistService.deletePlaylist(playlistId).subscribe(
      () => {
        this.playlists = this.playlists.filter((playlist) => playlist.id !== playlistId);
        console.log(`Playlist with ID ${playlistId} deleted successfully.`);
      },
      (error) => {
        console.error(`Error deleting playlist with ID ${playlistId}:`, error);
      });
  }
  

}


