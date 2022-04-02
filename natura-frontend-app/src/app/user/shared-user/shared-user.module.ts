import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { EntryCardComponent } from './entry-card/entry-card.component';


@NgModule({
  declarations: [
    EntryCardComponent
  ],
  imports: [
    SharedModule
  ],
  exports: [
    EntryCardComponent
  ],
})
export class SharedUserModule { }
