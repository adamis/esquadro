import { Component, OnInit, Input } from '@angular/core';
import {MenuItem} from 'primeng/api';

interface BreadCrumbItem {
  text: string;
  link?: string;
}

@Component({
  selector: 'app-bread-crumb',
  templateUrl: './bread-crumb.component.html',
  styleUrls: ['./bread-crumb.component.css']
})
export class BreadCrumbComponent implements OnInit {

  @Input() items: MenuItem[];
  home: MenuItem;

  constructor() { }

  ngOnInit() {
    this.home = {icon: 'pi pi-home', routerLink: '/home'};
  }

}
