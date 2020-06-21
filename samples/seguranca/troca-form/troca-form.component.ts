import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from 'primeng/api';
import { SegurancaService } from './../seguranca.service';
import { Usuarios } from './../../shared/models/usuarios';
import { Component } from '@angular/core';
import { BaseResourceListComponent } from 'src/app/shared/components/base-resource-list/base-resource-list.component';

@Component({
  selector: 'app-troca-form',
  templateUrl: './troca-form.component.html',
  styleUrls: ['./troca-form.component.css']
})
export class TrocaFormComponent extends BaseResourceListComponent<Usuarios> {

  constructor(
      private segurancaService: SegurancaService
    , public confirmationService: ConfirmationService
    , public messageService: MessageService
  ) {
    super(segurancaService, confirmationService, messageService);
  }

  alterarSenha(senha: string, novaSenha: string, repitaSenha: string) {
    console.log('senha: ' + senha);
    console.log('novaSenha: ' + novaSenha);
    console.log('repitaSenha: ' + repitaSenha);

    this.segurancaService.trocarSenha(sessionStorage.getItem('cpfCnpj'), senha, novaSenha, repitaSenha)

      .then(resultado => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso',
          detail: 'Senha alterada com Sucesso!', sticky: false, life: 15000 });
      })

      .catch(erroResponse => {
        this.messageService.add({
          severity: 'error', summary: 'Atenção',
          detail: erroResponse.error[0].mensagemUsuario, sticky: false, life: 15000
        });
      });

  }

}
