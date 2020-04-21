import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Species } from '../models/species.model';
import { SpeciesType } from '../models/type.enum';

@Injectable()
export class SpeciesService {

    private speciesUrl = '/natura-api/species';
 
  constructor(private http: HttpClient) { }

  getByType(type: SpeciesType): Observable<Species[]> {
    return this.http.get<Species[]>(this.speciesUrl + '/type/' + type);
  }
}