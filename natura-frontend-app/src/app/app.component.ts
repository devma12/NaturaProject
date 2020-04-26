import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'natura-frontend-app';

  username: string;

  userSubscription: Subscription;

  constructor (public authService: AuthService) {
    this.username = '';
  }

  ngOnInit() {
    this.userSubscription = this.authService.user.subscribe(value => {
      if (value) {
        this.username = value.username;
      } else {
        this.username = '';
      }
    });

    if (this.authService.getToken()) {
      this.authService.getAuthentication();
    }
  }

  logout() {
    this.authService.signout();
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }
}
