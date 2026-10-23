import { Routes } from '@angular/router';
import { ListListComponent } from './features/list-list/list-list.component';
import { ListCreateComponent } from './features/list-create/list-create.component';
import { ListDetailComponent } from './features/list-detail/list-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: '/lists', pathMatch: 'full' },
  { path: 'lists', component: ListListComponent },
  { path: 'lists/create', component: ListCreateComponent },
  { path: 'lists/:name', component: ListDetailComponent }
];
