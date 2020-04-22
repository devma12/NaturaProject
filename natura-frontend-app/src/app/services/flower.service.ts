import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Flower } from '../models/entries/flower.model';
import { User } from '../models/user.model';
import { Species } from '../models/species.model';

@Injectable()
export class FlowerService {

    private flowerUrl = '/natura-api/flower';
 
  constructor(private http: HttpClient) { }

  create(entryData: FormData): Observable<Flower> {
    return this.http.post<Flower>(this.flowerUrl + '/new', entryData);
  }
}