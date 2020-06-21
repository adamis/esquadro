import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Usuarios } from 'src/app/shared/models/usuarios';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent implements OnInit {

  constructor(private router: Router) { }

  display: boolean;

  user: Usuarios;

  items: MenuItem[];

  ngOnInit() {

    if (sessionStorage.getItem('usuario') === null) {
      this.router.navigate(['login']);
    } else {
      this.user = JSON.parse(sessionStorage.getItem('usuario'));
    }

    this.items = [
      {
        label: 'Cadastros',
        icon: 'pi pi-calendar-plus',
        items: [
        ]
      },
      {
        label: 'Atividades',
        icon: 'pi pi-fw pi-sort',
        items: [
          //TELA
        ]
      },
      {
        label: 'Consultas',
        icon: 'pi pi-fw pi-question',
        items: [
          //TELA
        ]
      },
      {
        label: 'Relatorios',
        icon: 'pi pi-fw pi-cog',
        items: [
          //TELA
        ]
      }
      ,
      {
        label: 'Ajuda',
        icon: 'pi pi-fw pi-info',
        items: [
          //TELA
        ]
      }
    ];
  }

  logoff() {
    sessionStorage.removeItem('usuario');
    this.router.navigate(['login']);
  }

}
