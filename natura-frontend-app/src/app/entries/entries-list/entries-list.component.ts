import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Entry } from 'src/app/models/entries/entry.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { EntryService } from 'src/app/services/entry.service';
import { FlowerService } from 'src/app/services/flower.service';
import { InsectService } from 'src/app/services/insect.service';
import { LoadingFromServerService } from 'src/app/services/loading-from-server.service';

@Component({
  selector: 'app-entries-list',
  templateUrl: './entries-list.component.html',
  styleUrls: ['./entries-list.component.scss']
})
export class EntriesListComponent implements OnInit, OnDestroy {

  typeSubscription: Subscription;

  type: SpeciesType;

  entries: Entry[];

  constructor(public loadingService: LoadingFromServerService,
              public entryService: EntryService,
              private route: ActivatedRoute,
              private router: Router,
              private flowerService: FlowerService,
              private insectService: InsectService) {
    this.loadingService.loading();
    this.entries = [];
   }

  ngOnInit(): void {
    this.typeSubscription = this.entryService.type$.subscribe(type => {
      
      this.entries.splice(0, this.entries.length);
  
      this.type = type;

      this.loadingService.loading();

      if (this.type === SpeciesType.Flower) {
        this.flowerService.getAll().subscribe(
          data => {
            this.entries = data;
            this.loadingService.loaded();
          }, 
          error => {
            this.loadingService.error('Failed to load flowers !');
          }
        );
      } else if (this.type === SpeciesType.Insect) {
        this.insectService.getAll().subscribe(
          data => {
            this.entries = data;
            this.loadingService.loaded();
          }, 
          error => {
            this.loadingService.error('Failed to load pollinating insects !');
          }
        );
      } else {
        this.router.navigate(['/not-found']);
      }
    });
    
  }

  ngOnDestroy(): void {
    this.loadingService.reset();
    this.typeSubscription.unsubscribe();
  }

}
