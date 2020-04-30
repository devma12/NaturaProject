import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Insect } from '../models/entries/insect.model';

@Injectable()
export class InsectService {

private insectUrl = '/natura-api/insect';
 
constructor(private http: HttpClient) { }

  create(entryData: FormData): Observable<Insect> {
    return this.http.post<Insect>(this.insectUrl + '/new', entryData);
  }

  getAll(): Observable<Insect[]> {
    return this.http.get<Insect[]>(this.insectUrl + '/all');
  }

}