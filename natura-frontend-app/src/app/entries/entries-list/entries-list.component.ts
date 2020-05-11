import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Entry } from 'src/app/models/entries/entry.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { FlowerService } from 'src/app/services/flower.service';
import { InsectService } from 'src/app/services/insect.service';
import { LoadingService } from 'src/app/services/loading.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-entries-list',
  templateUrl: './entries-list.component.html',
  styleUrls: ['./entries-list.component.scss']
})
export class EntriesListComponent implements OnInit, OnDestroy {

  paramSubscription: Subscription;

  type: SpeciesType;

  entries: Entry[];

  constructor(public loadingService: LoadingService,
              private route: ActivatedRoute,
              private router: Router,
              private flowerService: FlowerService,
              private insectService: InsectService) {
    this.loadingService.startLoading();
    this.entries = [];
   }

  ngOnInit(): void {
    this.paramSubscription = this.route.params.subscribe(params => {

      this.type = params['type'];

      this.loadingService.startLoading();

      if (this.type === SpeciesType.Flower) {
        this.flowerService.getAll().subscribe(
          data => {
            this.entries = data;
            this.loadingService.stopLoading();
          }, 
          error => {
            console.error('Failed to load flowers !');
          }
        );
      } else if (this.type === SpeciesType.Insect) {
        this.insectService.getAll().subscribe(
          data => {
            this.entries = data;
            this.loadingService.stopLoading();
          }, 
          error => {
            console.error('Failed to load pollinating insects !');
          }
        );
      } else {
        this.router.navigate(['/not-found']);
      }
    });
    
  }

  ngOnDestroy(): void {
    this.loadingService.stopLoading();
    this.paramSubscription.unsubscribe();
  }

}
