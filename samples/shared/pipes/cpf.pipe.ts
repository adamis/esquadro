import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cpf'
})
export class CpfPipe implements PipeTransform {

    transform(value: any): string {
        let cpf = '';
        let valueOf = '';

        if (typeof value !== 'string') {
            if (typeof value === 'number') {
                valueOf = value.toString();
            } else {
                return null;
            }
        } else {
            valueOf = value;
        }

        if (valueOf.length < 11) {
            let aux = '';
            for (let i = 0; i < 11 - valueOf.length; i++) {
                aux += '0';
            }
            valueOf = aux + valueOf;
        }

        for (let i = 0; i < valueOf.length; i++) {
            cpf += valueOf[i];
            if (i === 2 || i === 5) {
                cpf += '.';
            }
            if (i === 8) {
                cpf += '-';
            }
        }

        return cpf;
    }

}
