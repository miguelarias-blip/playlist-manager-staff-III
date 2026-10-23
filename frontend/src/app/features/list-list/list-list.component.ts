import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PlaylistService } from '../../core/services/playlist.service';
import { Playlist } from '../../core/models/playlist.model';

@Component({
  selector: 'app-list-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './list-list.component.html',
  styleUrls: ['./list-list.component.css']
})
export class ListListComponent implements OnInit {
  playlists: Playlist[] = [];
  loading = false;
  errorMessage = '';

  searchName = '';
  searchResult: Playlist | null = null;
  searchError = '';

  constructor(private service: PlaylistService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.errorMessage = '';
    this.service.getAll().subscribe({
      next: (data) => {
        this.playlists = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      }
    });
  }

  search(): void {
    const name = this.searchName.trim();
    if (!name) {
      return;
    }

    this.searchResult = null;
    this.searchError = '';

    this.service.getByName(name).subscribe({
      next: (data) => {
        this.searchResult = data;
      },
      error: (err) => {
        this.searchError = err.message || 'No se encontró la lista (404 Not Found)';
      }
    });
  }
}
