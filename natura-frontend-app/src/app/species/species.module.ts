import { AuthService } from '../services/auth.service';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { SpeciesListComponent } from './species-list/species-list.component';
import { SpeciesService } from '../services/species.service';
import { SpeciesComponent } from './species/species.component';
import { NewSpeciesComponent } from './new-species/new-species.component';
import { EnumToArrayPipe } from '../pipes/enum-to-array.pipe';

@NgModule({
    declarations: [
    SpeciesListComponent,
    SpeciesComponent,
    NewSpeciesComponent,
    EnumToArrayPipe
    ],
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
  