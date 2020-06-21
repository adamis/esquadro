import { TrocaFormComponent } from './troca-form/troca-form.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

import { LoginFormComponent } from './login-form/login-form.component';

const routes: Routes = [
  { path: 'troca', component: TrocaFormComponent },
  { path: '', component: LoginFormComponent }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class SegurancaRoutingModule { }
