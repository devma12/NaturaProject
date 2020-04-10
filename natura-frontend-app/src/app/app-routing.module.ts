import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './services/auth-guard.service';
import { NotFoundComponent } from './not-found/not-found.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
  { path: '', canActivate: [AuthGuard], component: HomeComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
