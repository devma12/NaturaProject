import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { EntryService } from 'src/app/services/entry.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.scss']
})
export class EntryComponent implements OnInit, OnDestroy {

  headerSubscription: Subscription;
  paramSubscription: Subscription;

  header: string = '';

  constructor(private route: ActivatedRoute,
              public entryService: EntryService) { }


  ngOnInit(): void {

    this.headerSubscription = this.entryService.type$.subscribe(value => {
      if (value !== null && value !== undefined)
        this.header = value;
      else
        this.header = '';
    });

    this.paramSubscription = this.route.params.subscribe(params => {
      const type = params['type'];
      this.entryService.setCurrentType(type);
    });

  }

  ngOnDestroy(): void {
    this.headerSubscription.unsubscribe();
    this.paramSubscription.unsubscribe();
    this.header = '';
  }

}
