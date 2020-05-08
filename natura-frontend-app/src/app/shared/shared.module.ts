import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialFileInputModule } from 'ngx-material-file-input';
import { AppMaterialModule } from '../shared/material.module';
import { FieldListItemComponent } from './field-list-item/field-list-item.component';
import { RouterModule } from '@angular/router';
import { SpinnerComponent } from './spinner/spinner.component';

@NgModule({
    declarations: [
      FieldListItemComponent,
      SpinnerComponent
    ],
    imports: [
      AppMaterialModule
    ],
    exports: [
      BrowserModule,
      BrowserAnimationsModule,
      AppMaterialModule,
      MaterialFileInputModule,
      FormsModule,
      ReactiveFormsModule,
      FlexLayoutModule,
      HttpClientModule,
      RouterModule,
      FieldListItemComponent,
      SpinnerComponent
    ],
    providers: []
  })
  export class SharedModule { }
  