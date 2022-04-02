import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from './account/account.component';
import { HerbariumComponent } from './herbarium/herbarium.component';
import { ProfileComponent } from './profile/profile.component';
import { SettingsComponent } from './settings/settings.component';

const accountRoutes: Routes = [
  {
    path: '',
    component: AccountComponent,
      children: [
        { path: 'profile', component: ProfileComponent },
        { path: 'settings', component: SettingsComponent },
        { path: 'herbarium', component: HerbariumComponent },
        { path: '**', redirectTo: 'not-found' }
      ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(accountRoutes)],
  exports: [RouterModule]
})
export class AccountRoutingModule { }
