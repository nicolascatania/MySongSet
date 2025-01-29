import { Song } from "./Song";

export interface Playlist {
    id: number;
    name: string;
    description: string;
    duration: string,
    songs: Song[];
  }
  