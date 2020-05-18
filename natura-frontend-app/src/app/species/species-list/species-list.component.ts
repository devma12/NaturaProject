import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { BehaviorSubject, Subject, Subscription } from 'rxjs';
import { Species } from 'src/app/models/species.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { LoadingService } from 'src/app/services/loading.service';
import { SpeciesService } from 'src/app/services/species.service';

@Component({
  selector: 'app-species-list',
  templateUrl: './species-list.component.html',
  styleUrls: ['./species-list.component.scss']
})
export class SpeciesListComponent implements OnInit, OnDestroy {

  species: Species[];
  species$: Subject<Species[]> = new BehaviorSubject<Species[]>([]);
  speciesSubscription: Subscription;

  displayedColumns: string[] = ['type', 'commonName', 'scientificName', 'family', 'order'];
  dataSource: MatTableDataSource<Species> = new MatTableDataSource<Species>([]);

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(public loadingService: LoadingService,
              private speciesService: SpeciesService) { 
    this.loadingService.startLoading();
  }

  ngOnInit(): void {
    // initialize table pagination & sorting
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    // subscribe to species$ subject to update data table
    this.speciesSubscription = this.species$.subscribe(values => {
      this.dataSource.data = values;
    });

    // Get all species from db
    this.speciesService.getAll().subscribe(
      data => {
        this.species = data;
        this.species$.next(data);
        this.loadingService.stopLoading();
      },
      error => {
        console.error('Failed to load species !');
      }
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  public onTypeChange(val: string) {
    let data: Species[];
    if (val === 'flower') {
      data = this.species.filter(s => s.type === SpeciesType.Flower);
    } else if (val === 'insect') {
      data = this.species.filter(s => s.type === SpeciesType.Insect);
    } else if (val === 'all') {
      data = this.species;
    }
    this.species$.next(data);
  }

  ngOnDestroy(): void {
    this.speciesSubscription.unsubscribe();
    this.loadingService.stopLoading();
  }

}
