import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Entry } from 'src/app/core/models/entries/entry.model';
import { Identification } from 'src/app/core/models/identification.model';
import { Species } from 'src/app/core/models/species.model';
import { SpeciesType } from 'src/app/core/models/type.enum';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { EntryService } from 'src/app/core/services/entry.service';
import { FlowerService } from 'src/app/core/services/flower.service';
import { IdentificationService } from 'src/app/core/services/identification.service';
import { InsectService } from 'src/app/core/services/insect.service';
import { LoadingFromServerService } from 'src/app/core/services/loading-from-server.service';
import { SpeciesService } from 'src/app/core/services/species.service';
import { ConfirmationComponent } from 'src/app/shared/confirmation/confirmation.component';
import { EntryUtils } from '../../shared-user/entry.utils';
import { ChooseSpeciesComponent } from './choose-species/choose-species.component';
import { CommentsComponent } from './comments/comments.component';
import { LikesComponent } from './likes/likes.component';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  isRouting: boolean = true;
  loaded: boolean = false;

  entry: Entry;
  type: SpeciesType;
  picture: any = {};

  identifications: Identification[] = [];
  displayedColumns: string[] = ['species.commonName', 'proposer', 'date', 'validated', 'validator', 'validatedDate', 'comments'];
  dataSource: MatTableDataSource<any> = new MatTableDataSource<Identification>([]);
  @ViewChild(MatSort) set matSort(sort: MatSort) {
    this.dataSource.sort = sort;
  }

  userSubscription: Subscription;
  canValidate: boolean = false;

  isValidated: boolean = false;
  species: string = '';

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
    this.identifications = [];

    // init data table sorting
    this.dataSource.sortingDataAccessor = (item, property) => {
      switch(property) {
        case 'species.commonName': return item.species && item.species.commonName ? item.species.commonName.toLowerCase() : '';
        case 'proposer': return item.suggestedBy && item.suggestedBy.username ? item.suggestedBy.username.toLowerCase() : '';
        case 'date': return item.suggestedDate;
        case 'validated': return item.validatedBy !== null && item.validatedBy !== undefined ? 'true' : 'false';
        case 'validator': return item.validatedBy && item.validatedBy.username ? item.validatedBy.username.toLowerCase() : '';
        default: return item[property];
      }
    };

    // get info from route params
    const id = this.route.snapshot.params['id'];
    this.type = this.entryService.getCurrentType();

    // evaluate logged user rights to validate entry identification
    this.userSubscription = this.authService.user.subscribe(user => {
      this.canValidate = false;
      if (user) {
        if (this.type === SpeciesType.Flower) {
          this.canValidate = user.flowerValidator;
        } else if (this.type === SpeciesType.Insect) {
          this.canValidate = user.insectValidator;
        }
      }
    });

    // get data details from server
    if (this.type === SpeciesType.Flower) {
      this.flowerService.getById(id).subscribe(
        data => {
          this.getEntryAndRelatedIdentifications(data);
          this.loaded = true;
        },
        error => {
          this.loadingService.error('Failed to load flower details !');
        }
      );
    } else if (this.type === SpeciesType.Insect) {
      this.insectService.getById(id).subscribe(
        data => {
          this.getEntryAndRelatedIdentifications(data);
          this.loaded = true;
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
    this.isValidated = this.entry.validated;
    this.getIdentifications();
  }

  getIdentifications() {
    this.identificationService.getByEntry(this.entry).subscribe(
      data => {
        this.identifications = data;
        this.dataSource.data = this.identifications;
        if (this.isValidated) {
          const validated = this.identifications.find(i => i && i.validatedBy);
          this.species = validated && validated.species ? validated.species.commonName : '';
        }
        this.loadingService.loaded();

        const identifying = this.route.snapshot.paramMap.get('identify');
        if (this.isRouting && identifying) {
          this.isRouting = false;
          this.openIdentificationDialog();
        }
      },
      error => {
        this.loadingService.error('Failed to load identifications !');
      }
    );
  }

  openIdentificationDialog(): void {
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
          if (result) {
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
                let msg: string = 'Failed to create new identification !';
                if (error.status === 406 || error.status === 404)
                  msg = error.error.message;
                this.loadingService.error(msg);
              }
            );
          }
        });
      }, error => {
        this.loadingService.error('Failed to load species !');
      }
    );

  }

  validate(identification, event) {
    if (this.isValidated) {
      this.loadingService.openErrorAlert('This item already has a validated identification.');
    } else {
      const index = this.identifications.indexOf(identification);
      const validator = this.authService.user.getValue();
      const species = identification.species;
      if (index === -1 || !validator || !species) {
        this.loadingService.openErrorAlert('An error occurred. Validation cannot be processed.');
      } else {

        if (this.canValidate && validator.id !== identification.suggestedBy.id) {
          // open popup to confirm validation
          const dialogRef = this.dialog.open(ConfirmationComponent, {
            data: `Are you sure you want to identify ${this.entry.name} as ${species.commonName} specimen ?`
          });

          // when popup is closed, send request to validate identification
          dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            if (result) {
              this.loadingService.loading();
              this.identificationService.validate(identification, validator).subscribe(
                data => {
                  console.log('validated !');
                  this.isValidated = true;
                  this.identifications[index] = data;
                  this.dataSource.data = this.identifications;
                  this.loadingService.loaded();
                },
                error => {
                  let msg: string = 'Failed to validate identification !';
                  if (error.status === 406)
                    msg = error.error.message;
                  this.loadingService.error(msg);
                }
              );
            }
          });

        } else {
          this.loadingService.openErrorAlert('You have not the permission to validate this identification.');
        }
      }
    }

    event.preventDefault();
  }

  openCommentDialog(identification: Identification): void {

    // open popup to enable user to suggest species to identify entry
    const dialogRef = this.dialog.open(CommentsComponent, {
      width: '500px',
      data: identification.comments
    });

    // when popup is closed
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {

        // get comment infos
        const commentData = new FormData();
        commentData.append('entry', identification.entry.id.toString());
        commentData.append('species', identification.species.id.toString());
        commentData.append('comment', result);

        // Get logged user id to be set as commentator
        const user: User = this.authService.user.getValue();
        if (user && user.id !== null && user.id !== undefined) {
          commentData.append('observer', user.id.toString());
        } else {
          commentData.append('observer', '-1');
        }

        this.loadingService.loading();
        this.identificationService.comment(commentData).subscribe(
          data => {
            identification.comments.push(data);
            this.loadingService.loaded();
          },
          error => {
            let msg: string = 'Failed to add new comment to identification !';
            if (error.status === 406 || error.status === 404)
              msg = error.error.message;
            this.loadingService.error(msg);
          }
        );
      }
    });

  }

  likeIdentification(identification: Identification) {
    // Get logged user id to be set as liker user
    const user: User = this.authService.user.getValue();

    this.loadingService.loading();
    this.identificationService.like(identification, user).subscribe(
      data => {
        identification.likes = data.likes;
        this.loadingService.loaded();
      },
      error => {
        let msg: string = 'Failed to like identification !';
        if (error.status === 406 || error.status === 404)
          msg = error.error.message;
        this.loadingService.error(msg);
      }
    );

  }

  viewLikes(identification: Identification): void {
    if (identification && identification.likes && identification.likes.length > 0) {
      // open popup to enable user to view who liked the identification
      const dialogRef = this.dialog.open(LikesComponent, {
        width: '300px',
        data: identification.likes
      });
    }
  }

  ngOnDestroy(): void {
    this.loadingService.reset();
    this.userSubscription.unsubscribe();
  }

}
