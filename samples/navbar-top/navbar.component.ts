import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent implements OnInit {

  constructor(private router: Router) { }

  display: boolean;

  items: MenuItem[];

    ngOnInit() {

        if (localStorage.getItem('usuario') === null) {
          this.router.navigate(['login']);
        }

        this.items = [
            {
                label: 'Cadastros',
                icon: 'pi pi-calendar-plus',
                items: [
													{label: 'Alunos', routerLink: 'alunos', command: (event) => {this.display = false; } },
                    {label: 'Cartoes', routerLink: 'cartoes', command: (event) => {this.display = false; } },
                ]
            },
            {
                label: 'Atividades',
                icon: 'pi pi-fw pi-sort',
                items: [
                    {label: 'Delete', icon: ''},
                    {label: 'Refresh', icon: ''}
                ]
            },
            {
                label: 'Consultas',
                icon: 'pi pi-fw pi-question',
                items: [
                    {
                        label: 'Contents'
                    },
                    {
                        label: 'Search',
                        icon: '',
                        items: [
                            {
                                label: 'Text',
                                items: [
                                    {
                                        label: 'Workspace'
                                    }
                                ]
                            },
                            {
                                label: 'File'
                            }
                    ]}
                ]
            },
            {
                label: 'Relatorios',
                icon: 'pi pi-fw pi-cog',
                items: [
                    {
                        label: 'Edit',
                        icon: '',
                        items: [
                            {label: 'Save', icon: ''},
                            {label: 'Update', icon: ''},
                        ]
                    },
                    {
                        label: 'Other',
                        icon: '',
                        items: [
                            {label: 'Delete', icon: ''}
                        ]
                    }
                ]
            }
            ,
            {
              label: 'Ajuda',
              icon: 'pi pi-fw pi-info',
              items: [
                  {
                      label: 'Edit',
                      icon: '',
                      items: [
                          {label: 'Save', icon: ''},
                          {label: 'Update', icon: ''},
                      ]
                  },
                  {
                      label: 'Other',
                      icon: '',
                      items: [
                          {label: 'Delete', icon: ''}
                      ]
                  }
              ]
          }
        ];
    }

    logoff() {
      localStorage.removeItem('usuario');
      this.router.navigate(['login']);
    }

}
