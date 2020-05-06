import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Entry } from '../models/entries/entry.model';
import { Identification } from '../models/identification.model';

@Injectable()
export class IdentificationService {

    private identificationUrl = '/natura-api/identification';
 
  constructor(private http: HttpClient) { }

  getByEntry(entry: Entry): Observable<Identification[]> {
    return this.http.get<Identification[]>(this.identificationUrl + '/entry/' + entry.id);
  }

  identify(identificationData: FormData): Observable<Identification> {
    return this.http.post<Identification>(this.identificationUrl + '/new', identificationData);
  }
}