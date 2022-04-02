import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/services/auth-guard.service';
import { HomeComponent } from './home/home.component';

const userRoutes: Routes = [
  { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
  { path: 'user-account', canActivate: [AuthGuard], 
      loadChildren: () =>
      import('./account/account.module').then(
        (m) => m.AccountModule
      )
  },
  { path: 'species', canActivate: [AuthGuard], 
    loadChildren: () =>
    import('./species/species.module').then(
      (m) => m.SpeciesModule
    )
  },
  { path: 'entries/:type', canActivate: [AuthGuard], 
    loadChildren: () =>
    import('./entries/entry.module').then(
      (m) => m.EntryModule
    )
  },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forChild(userRoutes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
