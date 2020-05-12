import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class EntryService {

    header$: BehaviorSubject<string> = new BehaviorSubject<string>('');

    constructor() { }
  
    setHeader(value: string) {
      this.header$.next(value);
    }

}