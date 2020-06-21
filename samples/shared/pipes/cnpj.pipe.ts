import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'cnpj'
})
export class CnpjPipe implements PipeTransform {

    transform(value: any): string {
        let cnpj = '';
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

        if (valueOf.length < 14) {
            let aux = '';
            for (let i = 0; i < 14 - valueOf.length; i++) {
                aux += '0';
            }
            valueOf = aux + valueOf;
        }

        for (let i = 0; i < valueOf.length; i++) {
            cnpj += valueOf[i];
            if (i === 1 || i === 4) {
                cnpj += '.';
            }
            if (i === 7) {
                cnpj += '/';
            }
            if (i === 11) {
                cnpj += '-';
            }
        }

        return cnpj;
    }

}
