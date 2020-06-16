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
import { AccountComponent } from './account/account.component';
import { ProfileComponent } from './account/profile/profile.component';
import { SettingsComponent } from './account/settings/settings.component';
import { HerbariumComponent } from './account/herbarium/herbarium.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
  { path: '', canActivate: [AuthGuard], component: HomeComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: 'entries/:type', canActivate: [AuthGuard], component: EntryComponent,
      children: [
        { path: 'new', component: CreateEntryComponent },
        { path: 'list', component: EntriesListComponent },
        { path: 'view/:id', component: ViewComponent },
      ]
  },
  { path: 'species', canActivate: [AuthGuard], component: SpeciesComponent,
      children: [
        { path: 'list', component: SpeciesListComponent },
        { path: 'new', component: NewSpeciesComponent },
      ]
  },
  { path: 'user-account', canActivate: [AuthGuard], component: AccountComponent,
      children: [
        { path: 'profile', component: ProfileComponent },
        { path: 'settings', component: SettingsComponent },
        { path: 'herbarium', component: HerbariumComponent },
      ]
  },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
