import { NgModule } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FlowerService } from '../services/flower.service';
import { InsectService } from '../services/insect.service';
import { SpeciesService } from '../services/species.service';
import { SharedModule } from '../shared/shared.module';
import { CreateEntryComponent } from './create-entry/create-entry.component';
import { NewEntryComponent } from './create-entry/new-entry/new-entry.component';
import { EntriesListComponent } from './entries-list/entries-list.component';
import { EntryCardComponent } from './entry-card/entry-card.component';
import { ViewComponent } from './view/view.component';
import { RouterModule } from '@angular/router';
import { IdentificationService } from '../services/identification.service';

@NgModule({
    declarations: [
    NewEntryComponent,
    CreateEntryComponent,
    EntriesListComponent,
    EntryCardComponent,
    ViewComponent
    ],
    imports: [
        SharedModule,
        RouterModule
    ],
    exports: [
    ],
    providers: [
        AuthService,
        FlowerService,
        SpeciesService,
        InsectService,
        IdentificationService
    ]
  })
  export class EntryModule { }
  