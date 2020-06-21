import { FormControl } from '@angular/forms';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-form-field-error',
  template: `
    <p class="ui-messages-error">
      {{errorMessage}}
    </p>
  `,
  styles: [`
    .ui-messages-error {
      margin: 0;
      margin-top: 4px;
      color: red;
    }
  `]
})
export class FormFieldErrorComponent implements OnInit {

  // tslint:disable-next-line:no-input-rename
  @Input('form-control') formControl: FormControl;
  @Input('header-colum') headerName: string;
  @Input('pattern') pattern: string;

  constructor() { }

  ngOnInit() {
  }

  public get errorMessage(): string | null {
    if (this.mustShowErrorMessage()) {
      return this.getErrorMessage();
    } else {
      return null;
    }
  }

  private mustShowErrorMessage(): boolean {
    return this.formControl.invalid && this.formControl.touched;
  }
  private getErrorMessage(): string | null {
    if (this.pattern != null && !this.formControl.value.match(this.pattern)) {
      return this.headerName + ' Fora do Padrão';
    } else if (this.formControl.errors.required) {
      return this.headerName + ' Obrigatório';
    } else if (this.formControl.errors.minlength) {
      const requiredLength = this.formControl.errors.minlength.requiredLength;
      return `Deve ter no mínimo ${requiredLength} caracteres`;
    } else if (this.formControl.errors.maxlength) {
      const requiredLength = this.formControl.errors.maxlength.requiredLength;
      return `Deve ter no máximo ${requiredLength} caracteres`;
    } else if (this.formControl.errors.email) {
      return 'formato de email inválido';
    }
  }
}
