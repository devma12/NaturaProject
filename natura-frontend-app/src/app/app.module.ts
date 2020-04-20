import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AuthGuard } from './services/auth-guard.service';
import { AuthService } from './services/auth.service';
import { RegisterComponent } from './register/register.component';
import { UserService } from './services/user.service';
import { SharedModule } from './shared/shared.module';
import { EntryModule } from './entries/entry.module';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    NotFoundComponent,
    RegisterComponent
  ],
  imports: [
    AppRoutingModule,
    SharedModule,
    EntryModule
  ],
  providers: [
    AuthService,
    AuthGuard,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
