import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from './services/auth.service';
import { LoadingService } from './services/loading.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'natura-frontend-app';

  loading: boolean = false;

  username: string;

  userSubscription: Subscription;
  loadingSubscription: Subscription;

  constructor (public authService: AuthService,
               public loadingService: LoadingService) {
    this.username = '';
  }

  ngOnInit() {
    this.userSubscription = this.authService.user.subscribe(user => {
      if (user) {
        this.username = user.username;
      } else {
        this.username = '';
      }
    });

    this.loadingSubscription = this.loadingService.loading$.subscribe((value) => {
      this.loading = value;
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
    this.loadingSubscription.unsubscribe();
  }
}
