import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { SharedUserModule } from '../shared-user/shared-user.module';
import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account/account.component';
import { HerbariumComponent } from './herbarium/herbarium.component';
import { ProfileComponent } from './profile/profile.component';
import { ChangeEmailComponent } from './settings/change-email/change-email.component';
import { ChangePasswordComponent } from './settings/change-password/change-password.component';
import { SettingsComponent } from './settings/settings.component';


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
    AccountRoutingModule,
    SharedUserModule
  ]
})
export class AccountModule { }
