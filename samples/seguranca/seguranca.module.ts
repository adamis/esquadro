import { ShowHidePasswordModule } from 'ngx-show-hide-password';
import { TableModule } from 'primeng/table';
import { SharedModule } from './../shared/shared.module';
import { TrocaFormComponent } from './troca-form/troca-form.component';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CaptchaModule } from 'primeng/captcha';
import { SegurancaRoutingModule } from './seguranca-routing.module';
import { LoginFormComponent } from './login-form/login-form.component';
import { ToastModule } from 'primeng/toast';
import { InputMaskModule } from 'primeng/inputmask';
import { IMaskModule } from 'angular-imask';

@NgModule({
  imports: [
    SharedModule,
    TableModule,
    CommonModule,
    IMaskModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    SegurancaRoutingModule,
    ShowHidePasswordModule,
    CaptchaModule,
    InputMaskModule,
    ToastModule
  ],
  declarations: [LoginFormComponent, TrocaFormComponent]
})
export class SegurancaModule { }
