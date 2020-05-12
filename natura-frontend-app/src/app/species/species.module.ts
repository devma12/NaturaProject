import { AuthService } from '../services/auth.service';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { SpeciesListComponent } from './species-list/species-list.component';
import { SpeciesService } from '../services/species.service';
import { SpeciesComponent } from './species/species.component';

@NgModule({
    declarations: [
    SpeciesListComponent,
    SpeciesComponent],
    imports: [
        SharedModule,
        RouterModule
    ],
    exports: [
    ],
    providers: [
        AuthService,
        SpeciesService
    ]
  })
  export class SpeciesModule { }
  