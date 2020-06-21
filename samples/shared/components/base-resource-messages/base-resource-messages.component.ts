import { MessageService } from 'primeng/api';
import { OnInit } from '@angular/core';
import {Message} from 'primeng/components/common/api';

export class BaseResourceMessages implements OnInit {

  constructor(private messageService: MessageService) { }
  ngOnInit() { }

  msgs: Message[] = [];

  showSuccess() {
      this.msgs = [];
      this.msgs.push({severity:'success', summary:'Success Message', detail:'Order submitted'});
  }

  showInfo() {
      this.msgs = [];
      this.msgs.push({severity:'info', summary:'Info Message', detail:'PrimeNG rocks'});
  }

  showWarn() {
      this.msgs = [];
      this.msgs.push({severity:'warn', summary:'Warn Message', detail:'There are unsaved changes'});
  }

  showError() {
      this.msgs = [];
      this.msgs.push({severity:'error', summary:'Error Message', detail:'Validation failed'});
  }

  showViaService() {
      this.messageService.add({severity:'success', summary:'Service Message', detail:'Via MessageService'});
  }

  clear() {
      this.msgs = [];
  }

}
