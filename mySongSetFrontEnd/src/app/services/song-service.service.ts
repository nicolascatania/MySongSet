import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Song } from '../../models/Song';

@Injectable({
  providedIn: 'root'
})
export class SongServiceService {

    private apiUrl = 'http://localhost:8080/songs'; 
    constructor(private http: HttpClient) {}

  getAllSongs(): Observable<Song[]> {
      return this.http.get<Song[]>(this.apiUrl);
  }

  saveSong(song: Song): Observable<Song> {
    return this.http.post<Song>(this.apiUrl, song); 
  }

  deleteSong(id: number): Observable<string> {
    const deleteUrl = `${this.apiUrl}/${id}`;
    return this.http.delete<string>(deleteUrl, { responseType: 'text' as 'json' });
  }
  
}
