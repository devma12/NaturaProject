import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Species } from 'src/app/models/species.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { FlowerService } from 'src/app/services/flower.service';
import { InsectService } from 'src/app/services/insect.service';
import { SpeciesService } from 'src/app/services/species.service';
import { EntryUtils } from '../entry.utils';
import { EntryService } from 'src/app/services/entry.service';

@Component({
  selector: 'app-create-entry',
  templateUrl: './create-entry.component.html',
  styleUrls: ['./create-entry.component.scss']
})
export class CreateEntryComponent implements OnInit {

  isLoading: boolean;
  type: SpeciesType;
  isFlower: boolean;

  species: Species[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private speciesService: SpeciesService,
              private authService: AuthService,
              private flowerService: FlowerService,
              private insectService: InsectService,
              private entryService: EntryService) {
    this.isLoading = true;
    this.species = [];
  }

  ngOnInit(): void {

    this.type = this.route.snapshot.params['type'];
    this.entryService.setHeader(this.type);

    this.isFlower = (this.type === SpeciesType.Flower);

    this.speciesService.getByType(this.type).subscribe(
      data => {
        this.species = data;
        this.isLoading = false;
      }, 
      error => {
        console.error(`Failed to load species by type ${this.type} !`);
      }
    );

  }

  createEntry(infos: any) {
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
          this.router.navigate(['/entries/view', this.type, data.id]);
        },
        error => {
          console.log('Failed to create new Flower entry.');
        }
      );
    } else if (this.type === SpeciesType.Insect) {
      this.insectService.create(entryData).subscribe(
        data => {
          this.router.navigate(['/entries/view', this.type, data.id]);
        },
        error => {
          console.log('Failed to create new Insect entry.');
        }
      );
    }
  }

}
