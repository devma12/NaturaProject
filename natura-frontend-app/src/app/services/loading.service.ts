import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class LoadingService {
    
    loading$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    constructor() { }
  
    startLoading() {
      this.loading$.next(true);
    }
  
    stopLoading() {
      this.loading$.next(false);
    }

}