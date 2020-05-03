import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EntryUtils } from '../entry.utils';
import { SpeciesType } from 'src/app/models/type.enum';
import { FlowerService } from 'src/app/services/flower.service';
import { InsectService } from 'src/app/services/insect.service';
import { Entry } from 'src/app/models/entries/entry.model';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit {

  isLoading: boolean;
  entry: Entry;
  picture: any = {};

  constructor(private route: ActivatedRoute,
    private router: Router,
    private flowerService: FlowerService,
    private insectService: InsectService) {
    this.isLoading = true;
  }

  ngOnInit(): void {

    const id = this.route.snapshot.params['id'];
    const type: SpeciesType = this.route.snapshot.params['type'];

    if (type === SpeciesType.Flower) {
      this.flowerService.getById(id).subscribe(
        data => {
          this.entry = data;
          this.picture = EntryUtils.getEntryPictureBase64Data(this.entry);
          this.isLoading = false;
        },
        error => {
          console.error('Failed to load flower details !');
        }
      );
    } else if (type === SpeciesType.Insect) {
      this.insectService.getById(id).subscribe(
        data => {
          this.entry = data;
          this.picture = EntryUtils.getEntryPictureBase64Data(this.entry);
          this.isLoading = false;
        },
        error => {
          console.error('Failed to load pollinating insect details !');
        }
      );
    } else {
      this.router.navigate(['/not-found']);
    }

  }

}
