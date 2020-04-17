import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { User } from './models/user.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'natura-frontend-app';

  username: string;

  constructor (public authService: AuthService) {
    this.username = '';
  }

  ngOnInit() {
    this.authService.user.subscribe(value => {
        if (value) {
          this.username = value.username;
        } else {
          this.username = '';
        }
      });
  }

  logout() {
    this.authService.signout();
  }
}
