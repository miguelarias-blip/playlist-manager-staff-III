import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Playlist } from '../models/playlist.model';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(`${this.apiUrl}/lists`);
  }

  getByName(name: string): Observable<Playlist> {
    return this.http.get<Playlist>(`${this.apiUrl}/lists/${encodeURIComponent(name)}`);
  }

  create(playlist: Playlist): Observable<Playlist> {
    return this.http.post<Playlist>(`${this.apiUrl}/lists`, playlist);
  }

  delete(name: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/lists/${encodeURIComponent(name)}`);
  }
}
