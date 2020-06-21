import { SegurancaService } from './../seguranca.service';
import { MessageService } from 'primeng/api';
import { Router, RouterLink } from '@angular/router';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ActivatedRoute, Params } from '@angular/router';


@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})

export class LoginFormComponent implements OnInit {

  empresa: string = null;
  selectedSistema: string = null;
  version = '';
  show = false;
  captcha = true;
  isVisible: boolean = false;
  masks = {
    mask: [
      {
        mask: '000.000.000-00'
      },
      {
        mask: '00.000.000/0000-00'
      }
    ]
  };


  ngOnInit(): void {
    if (sessionStorage.getItem('usuario') != null) {
      this.router.navigate(['home']);
    }
    this.version = environment.version;
    this.selectedSistema = environment.sistemaId;
  }

  constructor(
    private router: Router,
    private messageService: MessageService,
    private segurancaService: SegurancaService,
    private route: ActivatedRoute
  ) { }

  login(usuario: string, senha: string) {
    this.empresa = '1';
    if (this.captcha) {
      usuario = usuario.replace(/[^\d]+/g, '');
      usuario = usuario.replace('-', '');
      usuario = usuario.replace('/', '');

      this.segurancaService.login(usuario, senha, this.empresa, this.selectedSistema)
        .then(resultado => {
          console.log(resultado);

          if (resultado.response != null) {
            console.log(resultado.response.usuarios);
            sessionStorage.setItem('usuario', JSON.stringify(resultado.response.usuarios));
            //sessionStorage.setItem('empresa', this.empresa);
            //sessionStorage.setItem('sistema', this.selectedSistema);
            this.router.navigate(['home']);
          } else {
            this.captcha = false;
            (window as any).grecaptcha.reset();
            this.messageService.add({ severity: 'error', summary: 'Atenção', detail: resultado.error.message, sticky: false, life: 15000 });
          }
        }).catch(erroResponse => {
          console.log();
          this.captcha = false;
          (window as any).grecaptcha.reset();
          this.messageService.add({
            severity: 'error', summary: 'Atenção',
            detail: erroResponse.error.message, sticky: false, life: 15000
          });
        });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: "Confirme o 'Não sou um robô'", sticky: false, life: 8000 });
    }
  }

  showResponse(event) {
    this.captcha = true;
  }

  clickRecuperar(cpfCnpj) {
    console.log(cpfCnpj);
    this.segurancaService.recuperarSenhacpfCnpj(cpfCnpj.replace(/[^\d]+/g, ''))
      .then(resultado => {

        this.isVisible = !this.isVisible;
        this.messageService.add({ severity: 'info', summary: 'Atenção', detail: resultado.msg, sticky: false, life: 15000 });

      }).catch(erroResponse => {

        this.captcha = false;
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: erroResponse.error.erro, sticky: false, life: 15000 });

      });

  }
}


