import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Entry } from '../models/entries/entry.model';
import { Identification } from '../models/identification.model';
import { User } from '../models/user.model';

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

  validate(identification: Identification, user: User): Observable<Identification> {
    let userId: string = '-1';
    if (user && user.id !== null && user.id !== undefined) {
      userId = user.id.toString();
    }

    let entryId: string = '-1';
    if (identification && identification.entry && identification.entry.id !== null && identification.entry.id !== undefined) {
      entryId = identification.entry.id.toString();
    }

    let speciesId: string = '-1';
    if (identification && identification.species && identification.species.id !== null && identification.species.id !== undefined) {
      speciesId = identification.species.id.toString();
    }

    return this.http.post<Identification>(this.identificationUrl + '/validate', null, {
      params: {
        'entry': entryId,
        'species': speciesId,
        'validator': userId
      }
    });
  }
}