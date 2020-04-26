import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppMaterialModule } from '../shared/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { MaterialFileInputModule } from 'ngx-material-file-input';

@NgModule({
    declarations: [],
    imports: [],
    exports: [
      BrowserModule,
      BrowserAnimationsModule,
      AppMaterialModule,
      MaterialFileInputModule,
      FormsModule,
      ReactiveFormsModule,
      FlexLayoutModule,
      HttpClientModule
    ],
    providers: []
  })
  export class SharedModule { }
  