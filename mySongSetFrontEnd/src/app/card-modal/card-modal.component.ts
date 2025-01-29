import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Song } from '../../models/Song';
import { Instrument } from '../../models/Instrument';
import { InstrumentService } from '../services/instrument.service';
import { FormControl, FormGroup, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-card-modal',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './card-modal.component.html',
  styleUrls: ['./card-modal.component.css']
})
export class CardModalComponent {

  instruments: Instrument[] | null = null;
  onEditSong: boolean = false;

  @Input() song: Song | null = null;

  @Output() closeModalEvent = new EventEmitter<void>();

  @Output() deleteSongEvent = new EventEmitter<Song>();  

  closeModal() {
    this.closeModalEvent.emit();
  }

  deleteSong() {
    if (this.song) {
      this.deleteSongEvent.emit(this.song); 
    }
  }
  

}
