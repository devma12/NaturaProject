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
import { ChooseSpeciesComponent } from './view/choose-species/choose-species.component';
import { EntryComponent } from './entry/entry.component';
import { EntryService } from '../services/entry.service';

@NgModule({
    declarations: [
    NewEntryComponent,
    CreateEntryComponent,
    EntriesListComponent,
    EntryCardComponent,
    ViewComponent,
    ChooseSpeciesComponent,
    EntryComponent
    ],
    imports: [
        SharedModule,
        RouterModule
    ],
    exports: [
        EntryCardComponent
    ],
    providers: [
        AuthService,
        FlowerService,
        SpeciesService,
        InsectService,
        IdentificationService,
        EntryService
    ]
  })
  export class EntryModule { }
  