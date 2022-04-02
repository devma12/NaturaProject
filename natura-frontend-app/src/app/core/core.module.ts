import { HttpClientModule } from '@angular/common/http';
import { NgModule, Optional, SkipSelf } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PublicModule } from '../public/public.module';
import { SharedModule } from '../shared/shared.module';
import { UserModule } from '../user/user.module';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { SpinnerComponent } from './spinner/spinner.component';


@NgModule({
  declarations: [
    MainMenuComponent,
    SpinnerComponent
  ],
  imports: [
    SharedModule,
    PublicModule,
    UserModule
  ],
  exports: [
    MainMenuComponent,
    SpinnerComponent,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule
  ]
})
export class CoreModule { 
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded.');
    }
  }
}
