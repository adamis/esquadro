import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreadCrumbComponent } from './components/bread-crumb/bread-crumb.component';
import { RouterModule } from '@angular/router';
import { PageHeaderComponent } from './components/page-header/page-header.component';
import { FormFieldErrorComponent } from './components/form-field-error/form-field-error.component';
import { ServerErrorMessagesComponent } from './components/server-error-messages/server-error-messages.component';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { BaseResourceConfirmationComponent } from './components/base-resource-confirmation/base-resource-confirmation.component';
import { TelefonePipe } from './pipes/telefone.pipe';
import { CnpjPipe } from './pipes/cnpj.pipe';
import { CpfPipe } from './pipes/cpf.pipe';
import { SexoPipe } from './pipes/sexo.pipe';
import { SimNaoPipe } from './pipes/sim-nao.pipe';


@NgModule({
  declarations: [
  BreadCrumbComponent,
  PageHeaderComponent,
  FormFieldErrorComponent,
  ServerErrorMessagesComponent,
  BaseResourceConfirmationComponent,
  TelefonePipe,
  CpfPipe,
  CnpjPipe,
  SexoPipe,
  SimNaoPipe
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    BreadcrumbModule,
    MessagesModule,
    ButtonModule,
    ConfirmDialogModule,
    MessageModule

  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MessagesModule,
    MessageModule,
    BreadCrumbComponent,
    PageHeaderComponent,
    FormFieldErrorComponent,
    ServerErrorMessagesComponent,
	BaseResourceConfirmationComponent,
	TelefonePipe,
	CpfPipe,
	CnpjPipe,
	SexoPipe,
	SimNaoPipe

  ]
})
export class SharedModule { }
