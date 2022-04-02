import { NgModule } from '@angular/core';
import { DateAgoPipe } from 'src/app/core/pipes/date-ago.pipe';
import { SharedModule } from 'src/app/shared/shared.module';
import { SharedUserModule } from '../shared-user/shared-user.module';
import { CreateEntryComponent } from './create-entry/create-entry.component';
import { NewEntryComponent } from './create-entry/new-entry/new-entry.component';
import { EntriesListComponent } from './entries-list/entries-list.component';
import { EntryRoutingModule } from './entry-routing.module';
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
    EntryComponent,
    ViewComponent,
    ChooseSpeciesComponent,
    CommentsComponent,
    LikesComponent,
    DateAgoPipe
    ],
    imports: [
        SharedModule,
        SharedUserModule,
        EntryRoutingModule
    ],
    providers: [
        DateAgoPipe
    ]
  })
  export class EntryModule { }
  