import { NgModule } from '@angular/core';
import { EnumToArrayPipe } from 'src/app/core/pipes/enum-to-array.pipe';
import { SharedModule } from 'src/app/shared/shared.module';
import { MonthRangeSliderComponent } from './month-range-slider/month-range-slider.component';
import { NewSpeciesComponent } from './new-species/new-species.component';
import { SpeciesListComponent } from './species-list/species-list.component';
import { SpeciesRoutingModule } from './species-routing.module';
import { SpeciesComponent } from './species/species.component';

@NgModule({
    declarations: [
        SpeciesListComponent,
        SpeciesComponent,
        NewSpeciesComponent,
        EnumToArrayPipe,
        MonthRangeSliderComponent
    ],
    imports: [
        SharedModule,
        SpeciesRoutingModule
    ],
    providers: [
        EnumToArrayPipe
    ]
  })
  export class SpeciesModule { }
  