import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { NewFlowerComponent } from './flower/new-flower/new-flower.component';
import { FlowerService } from '../services/flower.service';
import { AuthService } from '../services/auth.service';

@NgModule({
    declarations: [
    NewFlowerComponent
    ],
    imports: [
        SharedModule
    ],
    exports: [
    ],
    providers: [
        AuthService,
        FlowerService
    ]
  })
  export class EntryModule { }
  