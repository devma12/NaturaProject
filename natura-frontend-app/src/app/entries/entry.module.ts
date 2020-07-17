import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DateAgoPipe } from '../pipes/date-ago.pipe';
import { AuthService } from '../services/auth.service';
import { EntryService } from '../services/entry.service';
import { FlowerService } from '../services/flower.service';
import { IdentificationService } from '../services/identification.service';
import { InsectService } from '../services/insect.service';
import { SpeciesService } from '../services/species.service';
import { SharedModule } from '../shared/shared.module';
import { CreateEntryComponent } from './create-entry/create-entry.component';
import { NewEntryComponent } from './create-entry/new-entry/new-entry.component';
import { EntriesListComponent } from './entries-list/entries-list.component';
import { EntryCardComponent } from './entry-card/entry-card.component';
import { EntryComponent } from './entry/entry.component';
import { ChooseSpeciesComponent } from './view/choose-species/choose-species.component';
import { CommentsComponent } from './view/comments/comments.component';
import { LikesComponent } from './view/likes/likes.component';
import { ViewComponent } from './view/view.component';

@NgModule({
    declarations: [
    NewEntryComponent,
    CreateEntryComponent,
    EntriesListComponent,
    EntryCardComponent,
    ViewComponent,
    ChooseSpeciesComponent,
    EntryComponent,
    CommentsComponent,
    LikesComponent,
    DateAgoPipe
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
        EntryService,
        DateAgoPipe
    ]
  })
  export class EntryModule { }
  