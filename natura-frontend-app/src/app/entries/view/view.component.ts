import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { Entry } from 'src/app/models/entries/entry.model';
import { Identification } from 'src/app/models/identification.model';
import { Species } from 'src/app/models/species.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { EntryService } from 'src/app/services/entry.service';
import { FlowerService } from 'src/app/services/flower.service';
import { IdentificationService } from 'src/app/services/identification.service';
import { InsectService } from 'src/app/services/insect.service';
import { LoadingFromServerService } from 'src/app/services/loading-from-server.service';
import { SpeciesService } from 'src/app/services/species.service';
import { EntryUtils } from '../entry.utils';
import { ChooseSpeciesComponent } from './choose-species/choose-species.component';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  isRouting: boolean = true;

  entry: Entry;
  type: SpeciesType;
  picture: any = {};

  identifications: Identification[] = [];
  displayedColumns: string[] = ['species', 'proposer', 'date', 'validated', 'validator', 'validationDate'];
  dataSource: MatTableDataSource<Identification>;

  constructor(private route: ActivatedRoute,
              private router: Router,
              public dialog: MatDialog,
              public loadingService: LoadingFromServerService,
              public entryService: EntryService,
              private authService: AuthService,
              private flowerService: FlowerService,
              private insectService: InsectService,
              private speciesService: SpeciesService,
              private identificationService: IdentificationService) {
    this.loadingService.loading();
  }

  ngOnInit(): void {

    const id = this.route.snapshot.params['id'];
    this.type = this.entryService.getCurrentType();

    if (this.type === SpeciesType.Flower) {
      this.flowerService.getById(id).subscribe(
        data => {
          this.getEntryAndRelatedIdentifications(data);
        },
        error => {
          this.loadingService.error('Failed to load flower details !');

        }
      );
    } else if (this.type === SpeciesType.Insect) {
      this.insectService.getById(id).subscribe(
        data => {
          this.getEntryAndRelatedIdentifications(data);
        },
        error => {
          this.loadingService.error('Failed to load pollinating insect details !');
        }
      );
    } else {
      this.router.navigate(['/not-found']);
    }
  }

  getEntryAndRelatedIdentifications(data: Entry) {
    this.entry = data;
    this.picture = EntryUtils.getEntryPictureBase64Data(this.entry);
    this.getIdentifications();
  }

  getIdentifications() {
    this.identificationService.getByEntry(this.entry).subscribe(
      data => {
        this.identifications = data;
        this.dataSource = new MatTableDataSource<Identification>(this.identifications);
        this.loadingService.loaded();

        const identifying = this.route.snapshot.paramMap.get('identify');
        if (this.isRouting && identifying) {
          this.isRouting = false;
          this.openDialog();
        }
      },
      error => {
        this.loadingService.error('Failed to load identifications !');
      }
    );
  }

  openDialog(): void {
    // get species by type
    this.speciesService.getByType(this.type).subscribe(
      data => {
        // open popup to enable user to suggest species to identify entry
        const dialogRef = this.dialog.open(ChooseSpeciesComponent, {
          height: '250px',
          width: '450px',
          data: data
        });

        // when popup is closed
        dialogRef.afterClosed().subscribe(result => {
          console.log('The dialog was closed');
          const species: Species = result;

          // save identification
          const identificationData = new FormData();
          identificationData.append('entryId', this.entry.id.toString());
          identificationData.append('speciesId', species.id.toString());

          // Get logged user id to be set as proposer user
          const user: User = this.authService.user.getValue();
          if (user && user.id !== null && user.id !== undefined) {
            identificationData.append('userId', user.id.toString());
          } else {
            identificationData.append('userId', '-1');
          }

          this.loadingService.loading();
          this.identificationService.identify(identificationData).subscribe(
            data => {
              this.getIdentifications();
            },
            error => {
              this.loadingService.error('Failed to create new identification !');
            }
          );

        });
      }, error => {
        this.loadingService.error('Failed to load species !');
      }
    );

  }

  ngOnDestroy(): void {
    this.loadingService.reset();
  }

}
