import { ConfirmationService } from 'primeng/api';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-base-resource-confirmation',
  templateUrl: './base-resource-confirmation.component.html',
  styleUrls: ['./base-resource-confirmation.component.css']
})
export class BaseResourceConfirmationComponent implements OnInit {

  constructor(public confirmationService: ConfirmationService) { }

  @Input('header') header: string;
  @Input('menssage') menssage: string;

  headers: string;
  menssages: string;

  ngOnInit() {
    this.headers = this.header;
    this.menssages = this.menssage;
  }

}
