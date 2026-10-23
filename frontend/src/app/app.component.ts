import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <header class="app-header">
      <nav class="nav">
        <a routerLink="/lists" class="brand">Playlist Manager</a>
        <div class="nav-links">
          <a routerLink="/lists" routerLinkActive="active">Listas</a>
          <a routerLink="/lists/create" routerLinkActive="active">Crear</a>
        </div>
      </nav>
    </header>
    <main class="container">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [
    `
      .app-header {
        background: #1f2937;
        color: #fff;
      }
      .nav {
        max-width: 960px;
        margin: 0 auto;
        padding: 14px 16px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .brand {
        color: #fff;
        font-size: 1.2rem;
        font-weight: 700;
        text-decoration: none;
      }
      .nav-links {
        display: flex;
        gap: 20px;
      }
      .nav-links a {
        color: #d1d5db;
        text-decoration: none;
        font-weight: 500;
      }
      .nav-links a:hover,
      .nav-links a.active {
        color: #fff;
      }
    `
  ]
})
export class AppComponent {}
