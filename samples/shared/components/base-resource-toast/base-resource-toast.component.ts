import { MessageService } from 'primeng/api';
import { OnInit } from '@angular/core';

export class BaseResourceToast implements OnInit {

  constructor(private messageService: MessageService) { }
  ngOnInit() { }

  showSuccess(summaryText: string, detailText: string) {
    this.messageService.add({ severity: 'success', summary: summaryText, detail: detailText });
  }

  showInfo(summaryText: string, detailText: string) {
    this.messageService.add({ severity: 'info', summary: summaryText, detail: detailText });
  }

  showWarn(summaryText: string, detailText: string) {
    this.messageService.add({ severity: 'warn', summary: summaryText, detail: detailText });
  }

  showError(summaryText: string, detailText: string) {
    this.messageService.add({ severity: 'error', summary: summaryText, detail: detailText });
  }

  showCustom(summaryText: string, detailText: string) {
    this.messageService.add({ key: 'custom', severity: 'info', summary: summaryText, detail: detailText });
  }

  showTopLeft(summaryText: string, detailText: string) {
    this.messageService.add({ key: 'tl', severity: 'info', summary: summaryText, detail: detailText });
  }

  showTopCenter(summaryText: string, detailText: string) {
    this.messageService.add({ key: 'tc', severity: 'warn', summary: summaryText, detail: detailText });
  }

  onReject(key: string) {
    this.messageService.clear(key);
  }

  clear() {
    this.messageService.clear();
  }

}
