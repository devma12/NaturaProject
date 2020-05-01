import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEntryComponent } from './entries/create-entry/create-entry.component';
import { EntriesListComponent } from './entries/entries-list/entries-list.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './services/auth-guard.service';
import { FieldListItemComponent } from './shared/field-list-item/field-list-item.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
  { path: '', canActivate: [AuthGuard], component: HomeComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: 'entries', canActivate: [AuthGuard],
      children: [
        { path: 'new', 
          children: [
            { path: 'flower', component: CreateEntryComponent },
            { path: 'insect', component: CreateEntryComponent }
          ]
        },
        { path: 'list', 
          children: [
            { path: 'flower', component: EntriesListComponent },
            { path: 'insect', component: EntriesListComponent }
          ]
        },
        { path: 'view/:id', component: FieldListItemComponent },
      ]
  },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
