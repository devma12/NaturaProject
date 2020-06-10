import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SpeciesType } from '../models/type.enum';

@Injectable()
export class EntryService {

    type$: BehaviorSubject<SpeciesType> = new BehaviorSubject<SpeciesType>(null);

    constructor() { }
  
    setCurrentType(value: SpeciesType) {
      this.type$.next(value);
    }

    getCurrentType(): SpeciesType {
      return this.type$.getValue();
    }

}