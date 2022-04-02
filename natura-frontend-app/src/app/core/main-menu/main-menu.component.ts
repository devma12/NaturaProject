import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss'],
})
export class MainMenuComponent implements OnInit, OnDestroy {
  
  username: string;
  userSubscription: Subscription;

  constructor(public authService: AuthService) {
    this.username = '';
  }

  ngOnInit(): void {
    this.userSubscription = this.authService.user.subscribe((user) => {
      if (user) {
        this.username = user.username;
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
