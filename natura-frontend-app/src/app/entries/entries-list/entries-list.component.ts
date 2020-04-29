import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, UrlSegment } from '@angular/router';
import { FlowerService } from 'src/app/services/flower.service';
import { InsectService } from 'src/app/services/insect.service';
import { Entry } from 'src/app/models/entries/entry.model';
import { EntryUtils } from '../entry.utils';
import { SpeciesType } from 'src/app/models/type.enum';

@Component({
  selector: 'app-entries-list',
  templateUrl: './entries-list.component.html',
  styleUrls: ['./entries-list.component.scss']
})
export class EntriesListComponent implements OnInit {

  isLoading: boolean;
  isFlower: boolean;

  entries: Entry[];

  constructor(private route: ActivatedRoute,
    private router: Router,
    private flowerService: FlowerService,
    private insectService: InsectService) {
    this.isLoading = true;
    this.entries = [];
   }

  ngOnInit(): void {
    let type: SpeciesType;
    try {
      type = EntryUtils.getEntryTypeFromRoute(this.route);
    } catch (e) {
      this.router.navigate(['/not-found']);
    }

    if (type === SpeciesType.Flower) {
      this.flowerService.getAll().subscribe(
        data => {
          this.entries = data;
          this.isLoading = false;
        }, 
        error => {
          console.error('Failed to load entries !');
        }
      );
    } else if (type === SpeciesType.Insect) {
      this.insectService.getAll().subscribe(
        data => {
          this.entries = data;
          this.isLoading = false;
        }, 
        error => {
          console.error('Failed to load entries !');
        }
      );
    } else {
      this.router.navigate(['/not-found']);
    }
  }

}
