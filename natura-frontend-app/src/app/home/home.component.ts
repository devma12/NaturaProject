import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

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

}
