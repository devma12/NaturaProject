import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Flower } from '../models/entries/flower.model';
import { User } from '../models/user.model';

@Injectable()
export class FlowerService {

    private flowerUrl = '/natura-api/flower';
 
  constructor(private http: HttpClient) { }

  create(imageData: FormData, name: string, user: User): Observable<Flower> {
    return this.http.post<Flower>(this.flowerUrl + '/new', imageData, {
        params: {
            'name': name,
            'createdBy': user.id.toString()
        }
    });
  }
}