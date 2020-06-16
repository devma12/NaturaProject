import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { AccountComponent } from './account.component';
import { AuthService } from '../services/auth.service';
import { ProfileComponent } from './profile/profile.component';
import { SettingsComponent } from './settings/settings.component';
import { HerbariumComponent } from './herbarium/herbarium.component';
import { ChangeEmailComponent } from './settings/change-email/change-email.component';
import { ChangePasswordComponent } from './settings/change-password/change-password.component';
import { EntryModule } from '../entries/entry.module';


@NgModule({
  declarations: [
    AccountComponent,
    ProfileComponent,
    SettingsComponent,
    HerbariumComponent,
    ChangeEmailComponent,
    ChangePasswordComponent
   ],
  imports: [
    SharedModule,
    RouterModule,
    EntryModule
  ],
  providers: [
    AuthService
  ]
})
export class UserModule { }
