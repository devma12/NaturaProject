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

@NgModule({
    declarations: [
      FieldListItemComponent
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
      FieldListItemComponent
    ],
    providers: []
  })
  export class SharedModule { }
  