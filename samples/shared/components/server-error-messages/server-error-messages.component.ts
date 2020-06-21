import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-server-error-messages',
  templateUrl: './server-error-messages.component.html',
  styleUrls: ['./server-error-messages.component.css']
})
export class ServerErrorMessagesComponent implements OnInit {

  // tslint:disable-next-line:no-input-rename
  @Input('server-error-messages') serverErrorMessages: string[] = null;

  constructor() { }

  ngOnInit() {
  }

}
