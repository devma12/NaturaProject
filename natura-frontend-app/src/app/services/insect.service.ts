import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Insect } from '../models/entries/insect.model';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class InsectService {

private flowerUrl = '/natura-api/flower';
 
constructor(private http: HttpClient) { }

create(imageData: FormData, name: string, user: User): Observable<Insect> {
  return this.http.post<Insect>(this.flowerUrl + '/new', imageData, {
      params: {
          'name': name,
          'createdBy': user.id.toString()
      }
  });
}
}