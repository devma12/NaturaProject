import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { UserService } from './user.service';

@Injectable()
export class AuthService {

    isAuth: boolean = false;
    user: User;
    
    constructor(private router: Router,
                private userService: UserService) { 
        this.user = null;
    }

    authenticate(email: string, password: string): Promise<User | void> {
        // To do later : redirect on previous page
        
        return this.userService.login(email, password).toPromise().then(
            value => {
                this.user = value;
                this.isAuth = true;
                this.router.navigate(['/home']);
            }
        );
    }

    register(email: string, username: string, password: string): Promise<User | void> {
        // To do : redirect on previous page
        
        return this.userService.register(email, username, password).toPromise().then(
            value => {
                this.user = value;
                this.isAuth = true;
                this.router.navigate(['/home']);
            }
        );
    }

    signout() {
        // To do : authenticate
        this.isAuth = false;
        this.router.navigate(['/login']);
    }

}