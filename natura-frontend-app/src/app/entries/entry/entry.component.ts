import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { EntryService } from 'src/app/services/entry.service';

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.scss']
})
export class EntryComponent implements OnInit, OnDestroy {

  headerSubscription: Subscription;

  header: string = '';

  constructor(public entryService: EntryService) { }


  ngOnInit(): void {
    this.headerSubscription = this.entryService.header$.subscribe(value => {
      this.header = value;
    });
  }

  ngOnDestroy(): void {
    this.headerSubscription.unsubscribe();
    this.header = '';
  }

}
