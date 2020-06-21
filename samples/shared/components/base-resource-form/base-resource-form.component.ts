import { MessageService } from 'primeng/api';
import {  OnInit, AfterContentChecked, Injector } from '@angular/core';

import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { switchMap } from 'rxjs/operators';

import { BaseResourceModel } from '../../models/base-resource.model';
import { BaseResourceService } from '../../services/base-resource.service';

export abstract class BaseResourceFormComponent<T extends BaseResourceModel> implements OnInit, AfterContentChecked {

  currentAction: string;
  resourceForm: FormGroup;
  pageTitle: string;
  serverErrorMessages: string[] = null;
  // tslint:disable-next-line:no-inferrable-types
  submittingForm: boolean = false;

  protected route: ActivatedRoute;
  protected router: Router;
  protected formBuilder: FormBuilder;

  constructor(
    protected injector: Injector,
    public resource: T,
    protected resourceService: BaseResourceService<T>,
    protected jsonDataToResourceFn: (jsonData) => T,
    private messageService: MessageService

  ) {
    this.route = this.injector.get(ActivatedRoute);
    this.router = this.injector.get(Router);
    this.formBuilder = this.injector.get(FormBuilder);
  }

  ngOnInit() {
    this.setCurrentAction();
    this.buildResourceForm();
    this.loadResource();
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngAfterContentChecked() {
    this.setPageTitle();

  }

  submitForm() {
    this.submittingForm = true;
    if (this.currentAction == 'new') {
      this.createResource();
    } else {
      this.updateResource();
    }
  }

  // PROTECTED METHODS

  protected setCurrentAction() {
    // tslint:disable-next-line:triple-equals
    if (this.route.snapshot.url[0].path == 'new') {
      this.currentAction = 'new';
    } else {
      this.currentAction = 'edit';
    }
  }

  protected loadResource() {
    // tslint:disable-next-line:triple-equals
    if (this.currentAction == 'edit') {
      this.route.paramMap.pipe(
        switchMap(params => this.resourceService.getById(+params.get('id')))
      )
      .subscribe(
        (resource) => {
          this.resource = resource;
          this.resourceForm.patchValue(resource);
        },
        (error) => alert('Ocorreu um erro no servidor, tente mais tarde.')
      );
    }
  }

  protected loadLocale() {
    return {
      firstDayOfWeek: 0,
      dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
      dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab'],
      dayNamesMin: ['Do', 'Se', 'Te', 'Qu', 'Qu', 'Se', 'Sa'],
      monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho',
                    'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
      monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
      today: 'Hoje',
      clear: 'Limpar'
    };
  }

  protected setPageTitle() {
    // tslint:disable-next-line:triple-equals
    if (this.currentAction == 'new') {
      this.pageTitle = this.creationPageTitle();
    } else {
      this.pageTitle = this.editionPageTitle();
    }
  }

  protected creationPageTitle(): string {
    return 'novo';
  }
  protected editionPageTitle(): string {
    return 'Edição';
  }

  protected createResource() {
    const resource: T = this.jsonDataToResourceFn(this.resourceForm.value);

    this.resourceService.create(resource)
      // tslint:disable-next-line:no-shadowed-variable
      .subscribe(result => {
        this.actionsForSuccess(result)
      },
        error => this.actionsForError(error)
      );
  }

  protected updateResource() {
    const resource: T = this.jsonDataToResourceFn(this.resourceForm.value);
    this.resourceService.update(resource)
    .subscribe(
      // tslint:disable-next-line:no-shadowed-variable
      resource => this.actionsForSuccess(resource),
      error => this.actionsForError(error)
    );
  }

  protected actionsForSuccess(resource: T) {

    // toastr.success('Solicitação processado com sucesso');
    this.messageService.add({severity: 'success', summary: 'Solicitação processado com sucesso',
    detail: 'Solicitação processado com sucesso'});
    const baseComponentPath = this.route.snapshot.parent.url[0].path;
    this.router.navigateByUrl(baseComponentPath, {skipLocationChange: true})
    .then(
      () => {
        console.log(this.router);
        return this.router.navigate(['/'+baseComponentPath]);
    }
    );
  }
  protected actionsForError(error) {
    // toastr.error('Ocorreu um erro ao processar a sua solicitação');
    this.messageService.add({severity: 'error', summary: 'Ocorreu um erro ao processar a sua solicitação',
    detail: 'Ocorreu um erro ao processar a sua solicitação'});
    this.submittingForm = false;

    if (error.status === 422) {
      this.serverErrorMessages = JSON.parse(error._body).errors;
    } else {
      this.serverErrorMessages = ['Falha na comunicação com o servidor, tente mais tarde!'];
    }
  }
  protected abstract buildResourceForm(): void;
}
