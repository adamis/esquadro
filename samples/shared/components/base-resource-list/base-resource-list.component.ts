import { BaseResourceConfirmationComponent } from './../base-resource-confirmation/base-resource-confirmation.component';
import { ConfirmationService } from 'primeng/api';
import { OnInit } from '@angular/core';
import { BaseResourceModel } from '../../models/base-resource.model';
import { BaseResourceService } from '../../services/base-resource.service';
import { LazyLoadEvent, MessageService } from 'primeng/api';

export abstract class BaseResourceListComponent<T extends BaseResourceModel> implements OnInit {

  resources: T[] = [];
  totalRegistros = 0;
  messageService;

  constructor(
    private resourceService: BaseResourceService<T>,
    public confirmationService: ConfirmationService,
    messageService: MessageService
  ) {
  }

  ngOnInit() { }

  delete(resource: T, funcOk: Function, funcFail: Function) {
    this.resourceService.delete(resource.id).subscribe(
      () => {
        this.resources = this.resources.filter(element => element !== resource);
        funcOk(this.messageService);
      },
      fail => {
        //console.log(fail);
        funcFail(fail, this.messageService);
      }
    );

  }
}
