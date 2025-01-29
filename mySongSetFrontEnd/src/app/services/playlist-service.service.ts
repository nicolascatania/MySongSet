import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Playlist } from '../../models/Playlist';
import { Song } from '../../models/Song';
import { PlaylistDTO } from '../../models/PlaylistDTO';

@Injectable({
  providedIn: 'root',
})
export class PlaylistServiceService {
  private apiUrl = 'http://localhost:8080/playlists';

  private selectedPlaylistSubject = new BehaviorSubject<Playlist | null>(null);
  selectedPlaylist$ = this.selectedPlaylistSubject.asObservable();

  private playlistsSubject = new BehaviorSubject<Playlist[]>([]);
  playlists$ = this.playlistsSubject.asObservable(); 



  constructor(private http: HttpClient) {}


  updatePlaylists() {
    this.getPlaylists().subscribe(
      (playlists) => this.playlistsSubject.next(playlists), 
      (error) => {
        console.error('Error fetching playlists:', error);
      }
    );
  }

  getPlaylists(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(this.apiUrl);
  }

  getSongsInPlaylist(playlistId: number): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.apiUrl}/${playlistId}/songs`);
  }

  getPlaylistById(id: number): Observable<Playlist> {
    return this.http.get<Playlist>(`${this.apiUrl}/${id}`);
  }

  createPlaylist(playlist: PlaylistDTO): Observable<Playlist> {
    return this.http.post<Playlist>(`${this.apiUrl}/create`, playlist);
  }

  addSongsToPlaylist(playlistID: number, songsID: number[]): Observable<Playlist>{
    return this.http.post<Playlist>(`${this.apiUrl}/${playlistID}/add-songs`, songsID);
  }

  updatePlaylist(id: number, playlist: PlaylistDTO): Observable<Playlist> {
    return this.http.put<Playlist>(`${this.apiUrl}/${id}`, playlist);
  }

  deletePlaylist(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  removeSongFromPlaylistId(playlistId: number, songId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${playlistId}/songs/${songId}`);
  }

  setSelectedPlaylist(playlist: Playlist | null): void {
    this.selectedPlaylistSubject.next(playlist);
  }

  getSelectedPlaylist(): Playlist | null {
    return this.selectedPlaylistSubject.getValue();
  }

  calculateTotalDuration(songs: Song[]): string {
    let totalSeconds = 0;

    songs.forEach((song) => {
      totalSeconds += this.convertDurationToSeconds(song.duration);
    });

    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;

    return `${this.padWithZero(hours)}:${this.padWithZero(minutes)}:${this.padWithZero(seconds)}`;
  }

  private padWithZero(number: number): string {
    return number < 10 ? `0${number}` : `${number}`;
  }

  convertDurationToSeconds(duration: string): number {
    const [mm, ss, ms] = duration.split(/[:.]/).map((part) => parseInt(part, 10));
    return mm * 60 + ss + ms / 1000;
  }
  

}
