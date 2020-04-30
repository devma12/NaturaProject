import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Flower } from '../models/entries/flower.model';

@Injectable()
export class FlowerService {

    private flowerUrl = '/natura-api/flower';
 
  constructor(private http: HttpClient) { }

  create(entryData: FormData): Observable<Flower> {
    return this.http.post<Flower>(this.flowerUrl + '/new', entryData);
  }

  getAll(): Observable<Flower[]> {
    return this.http.get<Flower[]>(this.flowerUrl + '/all');
  }

}