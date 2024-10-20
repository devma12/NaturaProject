import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard  {
    constructor(private authService: AuthService,
        private router: Router) { }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        if(this.authService.isAuth) {
            return true;
        } else {
            const url: string = state.url;
            if (url) {
                this.authService.redirectUrl = url;
            } else {
                this.authService.redirectUrl = '/home';
            }
            this.router.navigate(['/login'], { queryParams: { redirectUrl: state.url } });
        }
    }
}