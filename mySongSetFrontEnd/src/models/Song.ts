import { Instrument } from "./Instrument";

export interface Song {
    songId: number;
    title: string;
    artist: string;
    album: string;
    duration: string; 
    imageUrl: string;
    isManual: boolean;
    instruments: Instrument[]; 
  }
  