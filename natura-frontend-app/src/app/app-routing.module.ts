import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './services/auth-guard.service';
import { NotFoundComponent } from './not-found/not-found.component';
import { RegisterComponent } from './register/register.component';
import { NewFlowerComponent } from './entries/flower/new-flower/new-flower.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
  { path: '', canActivate: [AuthGuard], component: HomeComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: 'entries', canActivate: [AuthGuard],
      children: [
        { path: 'new-flower', component: NewFlowerComponent },
      ]
  },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
