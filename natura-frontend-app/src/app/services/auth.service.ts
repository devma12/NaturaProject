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
    }

    getToken(): string {
        const token = sessionStorage.getItem('token');
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
        sessionStorage.setItem('token', value.token);
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
                sessionStorage.setItem('token', null);
            },
            error => {
                console.error('Failed to logout');
            }
        );

    }

}