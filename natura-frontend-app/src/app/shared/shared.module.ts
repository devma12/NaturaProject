import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialFileInputModule } from 'ngx-material-file-input';
import { AppMaterialModule } from '../shared/material.module';

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
  