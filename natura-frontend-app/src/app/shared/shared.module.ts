import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@ngbracket/ngx-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { AppMaterialModule } from '../shared/material.module';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { FieldListItemComponent } from './field-list-item/field-list-item.component';

@NgModule({
  declarations: [FieldListItemComponent, ConfirmationComponent],
  imports: [AppMaterialModule, NgxSliderModule, CommonModule],
  exports: [
    CommonModule,
    AppMaterialModule,
    NgxSliderModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    RouterModule,
    FieldListItemComponent,
    ConfirmationComponent,
  ],
  providers: [],
})
export class SharedModule {}
