import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEntryComponent } from './entries/create-entry/create-entry.component';
import { EntriesListComponent } from './entries/entries-list/entries-list.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './services/auth-guard.service';
import { ViewComponent } from './entries/view/view.component';
import { SpeciesListComponent } from './species/species-list/species-list.component';
import { SpeciesComponent } from './species/species/species.component';
import { EntryComponent } from './entries/entry/entry.component';
import { NewSpeciesComponent } from './species/new-species/new-species.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
  { path: '', canActivate: [AuthGuard], component: HomeComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: 'entries', canActivate: [AuthGuard],
      children: [
        { path: 'new/:type', component: CreateEntryComponent },
        { path: 'list/:type', component: EntriesListComponent },
        { path: 'view/:type/:id', component: ViewComponent },
      ]
  },
  { path: 'species', canActivate: [AuthGuard], component: SpeciesComponent,
      children: [
        { path: 'list', component: SpeciesListComponent },
        { path: 'new', component: NewSpeciesComponent },
      ]
  },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
