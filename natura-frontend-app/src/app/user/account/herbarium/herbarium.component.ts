import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Flower } from 'src/app/core/models/entries/flower.model';
import { Insect } from 'src/app/core/models/entries/insect.model';
import { SpeciesType } from 'src/app/core/models/type.enum';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { EntryService } from 'src/app/core/services/entry.service';
import { FlowerService } from 'src/app/core/services/flower.service';
import { InsectService } from 'src/app/core/services/insect.service';
import { LoadingFromServerService } from 'src/app/core/services/loading-from-server.service';

@Component({
  selector: 'app-herbarium',
  templateUrl: './herbarium.component.html',
  styleUrls: ['./herbarium.component.scss']
})
export class HerbariumComponent implements OnInit, OnDestroy {

  user: User;

  flowers: Flower[];
  insects: Insect[];

  constructor(public loadingService: LoadingFromServerService,
              public entryService: EntryService,
              private authService: AuthService,
              private flowerService: FlowerService,
              private insectService: InsectService) {
    this.flowers = [];
    this.insects = [];
  }

  ngOnInit(): void {
    this.user = this.authService.user.getValue();
    this.loadEntries(SpeciesType.Flower);
  }

  loadEntries(type: SpeciesType) {

    this.loadingService.loading();

    if (type === SpeciesType.Flower) {
      this.flowerService.getByUser(this.user).subscribe(
        data => {
          this.flowers = data;
          this.loadingService.loaded();
        },
        error => {
          this.loadingService.error('Failed to load your flowers !');
        }
      );
    } else if (type === SpeciesType.Insect) {
      this.insectService.getByUser(this.user).subscribe(
        data => {
          this.insects = data;
          this.loadingService.loaded();
        },
        error => {
          this.loadingService.error('Failed to load your pollinating insects !');
        }
      );
    }
  }

  onTypeClick(event: MatTabChangeEvent) {
    if (event.index === 0) {
      this.loadEntries(SpeciesType.Insect);
    } else if (event.index === 1) {
      this.loadEntries(SpeciesType.Insect);
    }
  }

  ngOnDestroy(): void {
    this.loadingService.reset();
  }

}
