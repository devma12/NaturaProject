import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Insect } from '../models/entries/insect.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class InsectService {

private insectUrl = '/natura-api/insect';
 
constructor(private http: HttpClient) { }

  create(entryData: FormData): Observable<Insect> {
    return this.http.post<Insect>(this.insectUrl + '/new', entryData);
  }

  getAll(): Observable<Insect[]> {
    return this.http.get<Insect[]>(this.insectUrl + '/all');
  }

  getById(id: number): Observable<Insect> {
    return this.http.get<Insect>(this.insectUrl + '/' + id);
  }

  getByUser(user: User): Observable<Insect[]> {
    return this.http.get<Insect[]>(this.insectUrl + '/creator/' + user.id);
  }

}