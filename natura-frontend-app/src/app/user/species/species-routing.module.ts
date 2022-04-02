import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewSpeciesComponent } from './new-species/new-species.component';
import { SpeciesListComponent } from './species-list/species-list.component';
import { SpeciesComponent } from './species/species.component';

const speciesRoutes: Routes = [
  { path: '',
    component: SpeciesComponent,
    children: [
      { path: 'list', component: SpeciesListComponent },
      { path: 'new', component: NewSpeciesComponent },
      { path: '**', redirectTo: 'not-found' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(speciesRoutes)],
  exports: [RouterModule]
})
export class SpeciesRoutingModule { }
