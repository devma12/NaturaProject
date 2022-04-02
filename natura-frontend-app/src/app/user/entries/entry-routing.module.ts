import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEntryComponent } from './create-entry/create-entry.component';
import { EntriesListComponent } from './entries-list/entries-list.component';
import { EntryComponent } from './entry/entry.component';
import { ViewComponent } from './view/view.component';

const speciesRoutes: Routes = [
  {
    path: '',
    component: EntryComponent,
    children: [
      { path: 'new', component: CreateEntryComponent },
      { path: 'list', component: EntriesListComponent },
      { path: 'view/:id', component: ViewComponent },
      { path: '**', redirectTo: 'not-found' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(speciesRoutes)],
  exports: [RouterModule],
})
export class EntryRoutingModule {}
