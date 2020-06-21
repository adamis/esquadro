import { Title } from '@angular/platform-browser';
import { ConfirmationService, MessageService } from 'primeng/api';
import { HelperHttp } from './../seguranca/helper-http';
import { HttpClientModule } from '@angular/common/http';
import { NgModule, LOCALE_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { MenubarModule } from 'primeng/menubar';
import { SidebarModule } from 'primeng/sidebar';
import { RouterModule } from '@angular/router';
import { ErrorHandlerService } from './error-handler.service';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { PaginaNaoEncontradaComponent } from './pagina-nao-encontrada.component';

@NgModule({
  declarations: [NavbarComponent, PaginaNaoEncontradaComponent],
  imports: [
    CommonModule,
    SidebarModule,
    MenubarModule,
    HttpClientModule,
    RouterModule,
    PanelMenuModule,
    ConfirmDialogModule
  ],
  exports: [
    NavbarComponent,
    ConfirmDialogModule,
    ToastModule
  ]
  ,
  providers: [
    ErrorHandlerService,
    HelperHttp,
    ConfirmationService,
    MessageService,
    Title,
    { provide: LOCALE_ID, useValue: 'pt' }
  ]
})
export class CoreModule { }
