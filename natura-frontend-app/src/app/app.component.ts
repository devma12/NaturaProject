import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoadingService } from './core/services/loading.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'natura-frontend-app';

  loading: boolean = false;

  loadingSubscription: Subscription;

  constructor(public loadingService: LoadingService) {}

  ngOnInit() {
    this.loadingSubscription = this.loadingService.loading$.subscribe(
      (value) => {
        this.loading = value;
      }
    );
  }

  ngOnDestroy(): void {
    this.loadingSubscription.unsubscribe();
  }
}
