import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Species } from 'src/app/models/species.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { EntryService } from 'src/app/services/entry.service';
import { FlowerService } from 'src/app/services/flower.service';
import { InsectService } from 'src/app/services/insect.service';
import { LoadingFromServerService } from 'src/app/services/loading-from-server.service';
import { SpeciesService } from 'src/app/services/species.service';

@Component({
  selector: 'app-create-entry',
  templateUrl: './create-entry.component.html',
  styleUrls: ['./create-entry.component.scss']
})
export class CreateEntryComponent implements OnInit, OnDestroy {

  type: SpeciesType;
  isFlower: boolean;

  loaded: boolean;

  species: Species[];

  constructor(private router: Router,
              private speciesService: SpeciesService,
              private authService: AuthService,
              public loadingService: LoadingFromServerService,
              private flowerService: FlowerService,
              private insectService: InsectService,
              private entryService: EntryService) {
    this.species = [];
    this.loadingService.loading();
    this.loaded = false;
  }

  ngOnInit(): void {
    
    this.type = this.entryService.getCurrentType();

    this.isFlower = (this.type === SpeciesType.Flower);

    this.speciesService.getByType(this.type).subscribe(
      data => {
        this.species = data;
        this.loadingService.loaded();
        this.loaded = true;
      }, 
      error => {
        this.loadingService.error(`Failed to load species by type ${this.type} !`);
        this.loaded = true;
      }
    );

  }

  createEntry(infos: any) {
    this.loadingService.loading();
    // Create Form Data to send picture and others entry infos
    const selectedFile: File = infos['selectedFile'];
    const entryData = new FormData();
    entryData.append('imageFile', selectedFile, selectedFile.name);

    // Get entry details
    entryData.append('name', infos['name']);
    entryData.append('date', infos['date']);
    entryData.append('description', infos['description']);
    entryData.append('location', infos['location']);

    // Get species id
    const species: Species = infos['species'];
    if (species && species.id !== null && species.id !== undefined) {
      entryData.append('species', species.id.toString());
    } else {
      entryData.append('species', '-1');
    }

    // Get logged user id to be set as creator
    const user: User = this.authService.user.getValue();
    if (user && user.id !== null && user.id !== undefined) {
      entryData.append('createdBy', user.id.toString());
    } else {
      entryData.append('createdBy', '-1');
    }

    // Create new entry
    if (this.type === SpeciesType.Flower) {
      this.flowerService.create(entryData).subscribe(
        data => {
          this.loadingService.loaded();
          this.router.navigate(['/entries', this.type, 'view', data.id]);
        },
        error => {
          this.loadingService.error('Failed to create new flower entry.');
        }
      );
    } else if (this.type === SpeciesType.Insect) {
      this.insectService.create(entryData).subscribe(
        data => {
          this.loadingService.loaded();
          this.router.navigate(['/entries', this.type, 'view', data.id]);
        },
        error => {
          this.loadingService.error('Failed to create new insect entry.');
        }
      );
    }
  }

  ngOnDestroy(): void {
    this.loadingService.reset();
  }

}
