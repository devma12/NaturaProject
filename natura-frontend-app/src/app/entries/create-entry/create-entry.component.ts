import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, UrlSegment } from '@angular/router';
import { SpeciesType } from 'src/app/models/type.enum';
import { SpeciesService } from 'src/app/services/species.service';
import { Species } from 'src/app/models/species.model';
import { FlowerService } from 'src/app/services/flower.service';
import { AuthService } from 'src/app/services/auth.service';
import { User } from 'src/app/models/user.model';
import { InsectService } from 'src/app/services/insect.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-create-entry',
  templateUrl: './create-entry.component.html',
  styleUrls: ['./create-entry.component.css']
})
export class CreateEntryComponent implements OnInit {

  isLoading: boolean;
  isFlower: boolean;

  species: Species[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private speciesService: SpeciesService,
              private authService: AuthService,
              private flowerService: FlowerService,
              private insectService: InsectService) {
    this.isLoading = true;
  }

  ngOnInit(): void {
    let type: SpeciesType;
    const url: UrlSegment = this.route.snapshot.url[this.route.snapshot.url.length - 1];
    console.log(url.toString());
    if (url.toString() === 'flower') {
      this.isFlower = true;
      type = SpeciesType.Flower;
    } else if (url.toString() === 'insect') {
      this.isFlower = false;
      type = SpeciesType.Insect;
    } else {
      this.router.navigate(['/not-found']);
    }

    this.speciesService.getByType(type).subscribe(
      data => {
        this.species = data;
        this.isLoading = false;
      }, 
      error => {
        console.error('Failed to load species by type !');
      }
    );

  }

  createEntry(infos: any) {
    // Create Form Data to send picture
    const selectedFile: File = infos['selectedFile'];
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', selectedFile, selectedFile.name);

    // Get logged user to be set as creator
    const user: User = this.authService.user.getValue();

    // Get entry name
    const name: string = infos['name'];

    // Create new entry
    if (this.isFlower) {
      this.flowerService.create(uploadImageData, name, user).subscribe(
        data => {
          this.router.navigate(['/home']);
        },
        error => {
          console.log('Failed to create new Flower entry.');
        }
      );;
    } else {
      this.insectService.create(uploadImageData, name, user).subscribe(
        data => {
          this.router.navigate(['/home']);
        },
        error => {
          console.log('Failed to create new Flower entry.');
        }
      );;
    }
  }

}
