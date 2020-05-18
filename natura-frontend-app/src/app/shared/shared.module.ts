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
import { Ng5SliderModule } from 'ng5-slider';

@NgModule({
    declarations: [
      FieldListItemComponent,
      SpinnerComponent
    ],
    imports: [
      AppMaterialModule,
      Ng5SliderModule
    ],
    exports: [
      BrowserModule,
      BrowserAnimationsModule,
      AppMaterialModule,
      Ng5SliderModule,
      MaterialFileInputModule,
      FormsModule,
      ReactiveFormsModule,
      FlexLayoutModule,
      HttpClientModule
    ],
    providers: []
  })
  export class SharedModule { }
  