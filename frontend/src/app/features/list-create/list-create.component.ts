import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PlaylistService } from '../../core/services/playlist.service';

@Component({
  selector: 'app-list-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './list-create.component.html',
  styleUrls: ['./list-create.component.css']
})
export class ListCreateComponent {
  form: FormGroup;
  submitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private service: PlaylistService
  ) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, this.notBlank]],
      descripcion: [''],
      canciones: this.fb.array([])
    });
    this.addSong();
  }

  get canciones(): FormArray {
    return this.form.get('canciones') as FormArray;
  }

  private notBlank(control: AbstractControl): ValidationErrors | null {
    return (control.value || '').trim() === '' ? { blank: true } : null;
  }

  newSong(): FormGroup {
    return this.fb.group({
      titulo: [''],
      artista: [''],
      album: [''],
      anno: [''],
      genero: ['']
    });
  }

  addSong(): void {
    this.canciones.push(this.newSong());
  }

  removeSong(index: number): void {
    this.canciones.removeAt(index);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    const playlist = {
      nombre: this.form.value.nombre.trim(),
      descripcion: this.form.value.descripcion,
      canciones: this.form.value.canciones
    };

    this.service.create(playlist).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Lista creada correctamente (201 Created)';
        this.form.reset();
        this.canciones.clear();
        this.addSong();
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.message || 'Error al crear la lista (400 Bad Request)';
      }
    });
  }
}
