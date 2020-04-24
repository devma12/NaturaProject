import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { User } from '../models/user.model';
import { UserService } from './user.service';

@Injectable()
export class AuthService {

    isAuth: boolean = false;
    user: BehaviorSubject<User>;
    redirectUrl: string;
    
    constructor(private router: Router,
        private userService: UserService) {
        this.user = new BehaviorSubject<User>(null);

        const credentials = window.sessionStorage.getItem('JWT_TOKEN');
        console.log(`token: ${credentials}`);

        window.addEventListener('storage', (event) => {
            
            if (event.key === 'REQUESTING_TOKEN' && credentials) {
                console.log('share token');
              window.localStorage.setItem('SHARING_TOKEN', credentials);
              window.localStorage.removeItem('SHARING_TOKEN');
            }

            if (event.key === 'SHARING_TOKEN' && !credentials) {
              window.sessionStorage.setItem('JWT_TOKEN', event.newValue);
              console.log('get requested token');
              this.getAuthentication();
            }
          });

          if (!credentials) {
            console.log('request token');
            window.localStorage.setItem('REQUESTING_TOKEN', '');
            window.localStorage.removeItem('REQUESTING_TOKEN');
          }
    }

    getToken(): string {
        const token = sessionStorage.getItem('JWT_TOKEN');
        console.log(`token: ${token}`);
        return token;
    }

    login(username: string, password: string): Promise<User | void> {
        
        return this.userService.login(username, password).toPromise().then(
            value => {
                this.authenticate(value);
            }
        );
    }

    register(email: string, username: string, password: string): Promise<User | void> {
        
        return this.userService.register(email, username, password).toPromise().then(
            value => {
                this.authenticate(value);
            }
        );
    }

    getAuthentication() {
        this.userService.getAuthentication().subscribe(
            value => {
                this.authenticate(value);
            },
            error => {
                console.error('Failed to get authentication, token may be invalid.');
            }
        );
    }

    private authenticate(value: User) {
        sessionStorage.setItem('JWT_TOKEN', value.token);
        this.user.next(value);
        this.isAuth = true;
        this.router.navigate([this.redirectUrl]);
        this.redirectUrl = null;
    }

    signout() {
        this.userService.logout().subscribe(
            value => {
                this.user.next(null);
                this.isAuth = false;
                this.router.navigate(['/login']);
                sessionStorage.setItem('JWT_TOKEN', null);
            },
            error => {
                console.error('Failed to logout');
            }
        );

    }

}