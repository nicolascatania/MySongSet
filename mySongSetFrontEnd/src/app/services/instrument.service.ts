import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Instrument } from '../../models/Instrument';

@Injectable({
  providedIn: 'root'
})
export class InstrumentService {


  private apiUrl = 'http://localhost:8080/instruments';

  constructor(private http: HttpClient) { }


  getAllInstruments(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(`${this.apiUrl}`);
  }
  

}
