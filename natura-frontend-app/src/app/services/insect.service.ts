import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Insect } from '../models/entries/insect.model';
import { HttpClient } from '@angular/common/http';
import { Species } from '../models/species.model';

@Injectable()
export class InsectService {

private flowerUrl = '/natura-api/insect';
 
constructor(private http: HttpClient) { }

  create(entryData: FormData): Observable<Insect> {
    return this.http.post<Insect>(this.flowerUrl + '/new', entryData);
  }
}