import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { PublicRoutingModule } from './public-routing.module';
import { RegisterComponent } from './register/register.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    NotFoundComponent
  ],
  imports: [
    SharedModule,
    PublicRoutingModule
  ]
})
export class PublicModule { }
