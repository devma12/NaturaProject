import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Species } from '../models/species.model';
import { SpeciesType } from '../models/type.enum';

@Injectable()
export class SpeciesService {

    private speciesUrl = '/natura-api/species';
 
  constructor(private http: HttpClient) { }

  
  getAll(): Observable<Species[]> {
    return this.http.get<Species[]>(this.speciesUrl + '/all');
  }
  
  getByType(type: SpeciesType): Observable<Species[]> {
    return this.http.get<Species[]>(this.speciesUrl + '/type/' + type);
  }

  create(species: Species): Observable<Species> {
    return this.http.post<Species>(this.speciesUrl + '/new', species);
  }

}