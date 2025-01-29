import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { PlaylistDTO } from '../../models/PlaylistDTO';

@Component({
  selector: 'app-playlist-modal',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './playlist-modal.component.html',
  styleUrl: './playlist-modal.component.css'
})
export class PlaylistModalComponent {
  @Input() playlistToEdit?: PlaylistDTO;  
  @Output() submitForm = new EventEmitter<PlaylistDTO>();  
  @Output() closeModalEvent = new EventEmitter<void>(); 


  form: FormGroup;

  constructor() {
    this.form = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(30),
      ]),
      description: new FormControl('', [
        Validators.required,
        Validators.maxLength(200),
      ]),
    });
  }

  ngOnInit(): void {
    if (this.playlistToEdit) {
      this.form.patchValue({
        name: this.playlistToEdit.name,
        description: this.playlistToEdit.description,
      });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.submitForm.emit(this.form.value);  
    }
  }

  cancel(): void {
    this.closeModalEvent.emit(); 
  }


}
