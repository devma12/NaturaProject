import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class AuthService {

    isAuth: boolean = false;
    
    constructor(private router: Router) { }

    authenticate(email: string, password: string) {
        // To do : authenticate + redirect on previous page
        this.isAuth = true;
        this.router.navigate(['/home']);
    }

    signout() {
        // To do : authenticate
        this.isAuth = false;
        this.router.navigate(['/login']);
    }

}