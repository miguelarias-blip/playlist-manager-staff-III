import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PlaylistService } from '../../core/services/playlist.service';
import { Playlist } from '../../core/models/playlist.model';

@Component({
  selector: 'app-list-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './list-detail.component.html',
  styleUrls: ['./list-detail.component.css']
})
export class ListDetailComponent implements OnInit {
  name = '';
  playlist: Playlist | null = null;
  loading = false;
  deleting = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: PlaylistService
  ) {}

  ngOnInit(): void {
    this.name = this.route.snapshot.paramMap.get('name') ?? '';
    this.load();
  }

  load(): void {
    this.loading = true;
    this.errorMessage = '';
    this.service.getByName(this.name).subscribe({
      next: (data) => {
        this.playlist = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message || 'No se encontró la lista (404 Not Found)';
        this.loading = false;
      }
    });
  }

  delete(): void {
    if (!this.playlist) {
      return;
    }

    this.deleting = true;
    this.errorMessage = '';
    this.service.delete(this.playlist.nombre).subscribe({
      next: () => {
        this.deleting = false;
        this.router.navigate(['/lists']);
      },
      error: (err) => {
        this.deleting = false;
        this.errorMessage = err.message;
      }
    });
  }
}
