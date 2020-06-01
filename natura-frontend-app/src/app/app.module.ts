import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { EntryModule } from './entries/entry.module';
import { HomeComponent } from './home/home.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './services/auth-guard.service';
import { AuthService } from './services/auth.service';
import { UserService } from './services/user.service';
import { SharedModule } from './shared/shared.module';
import { SpeciesModule } from './species/species.module';
import { LoadingService } from './services/loading.service';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { AlertService } from './services/alert.service';
import { LoadingFromServerService } from './services/loading-from-server.service';


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
    EntryModule,
    SpeciesModule
  ],
  providers: [
    AuthService,
    AuthGuard,
    UserService,
    LoadingService,
    AlertService,
    LoadingFromServerService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {provide: MAT_DATE_LOCALE, useValue: 'fr'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
