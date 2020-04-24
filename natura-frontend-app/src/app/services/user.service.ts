import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable()
export class UserService {

  private userUrl = '/natura-api/user';

  constructor(private http: HttpClient) { }

  register(email: string, username: string, pwd: string): Observable<User> {
    return this.http.post<User>(this.userUrl + '/register', null, {
      params: {
        'email': email,
        'username': username,
        'password': pwd
      }
    });
  }

  login(username: string, pwd: string): Observable<User> {
    return this.http.post<User>(this.userUrl + '/login', null, {
      params: {
        'username': username,
        'password': pwd
      }
    });
  }

  getAuthentication(): Observable<User> {
    return this.http.get<User>(this.userUrl + '/authenticate');
  }

  logout() {
    return this.http.post<User>(this.userUrl + '/logout', null);
  }

}