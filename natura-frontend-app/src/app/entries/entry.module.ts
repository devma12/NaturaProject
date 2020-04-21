import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { NewEntryComponent } from './create-entry/new-entry/new-entry.component';
import { FlowerService } from '../services/flower.service';
import { AuthService } from '../services/auth.service';
import { CreateEntryComponent } from './create-entry/create-entry.component';
import { SpeciesService } from '../services/species.service';
import { InsectService } from '../services/insect.service';

@NgModule({
    declarations: [
    NewEntryComponent,
    CreateEntryComponent
    ],
    imports: [
        SharedModule
    ],
    exports: [
    ],
    providers: [
        AuthService,
        FlowerService,
        SpeciesService,
        InsectService
    ]
  })
  export class EntryModule { }
  