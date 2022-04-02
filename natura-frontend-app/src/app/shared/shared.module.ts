import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Ng5SliderModule } from 'ng5-slider';
import { MaterialFileInputModule } from 'ngx-material-file-input';
import { AppMaterialModule } from '../shared/material.module';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { FieldListItemComponent } from './field-list-item/field-list-item.component';

@NgModule({
    declarations: [
      FieldListItemComponent,
      ConfirmationComponent
    ],
    imports: [
      AppMaterialModule,
      Ng5SliderModule,
      CommonModule
    ],
    exports: [
      CommonModule,
      AppMaterialModule,
      Ng5SliderModule,
      MaterialFileInputModule,
      FormsModule,
      ReactiveFormsModule,
      FlexLayoutModule,
      RouterModule,
      FieldListItemComponent,
      ConfirmationComponent
    ],
    providers: []
  })
  export class SharedModule { }
  