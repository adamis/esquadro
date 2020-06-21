import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {CaptchaModule} from 'primeng/captcha';
import { CoreModule } from './core/core.module';
import {SidebarModule} from 'primeng/sidebar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MenubarModule } from 'primeng/menubar';
import { SharedModule } from './shared/shared.module';

@NgModule({

  declarations: [
    AppComponent
  ],
  imports: [
    CoreModule,
    SharedModule,
    BrowserAnimationsModule,
    SidebarModule,
    MenubarModule,
    BrowserModule,
    AppRoutingModule,
	CaptchaModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [ ]
})
export class AppModule { }
