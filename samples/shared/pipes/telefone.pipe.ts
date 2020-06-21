import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'telefone'
})
export class TelefonePipe implements PipeTransform {

    /**
     * Mostra o telefone com a máscara
     *
     * Parâmetros:
     * @any value: somente números
     * @string type: 'F' fixo ou 'C' celular
     */
    transform(value: any, type: string): string {
        let telefone = '';
        let valueOf = '';
        let lenghtVal: number;

        if (typeof value !== 'string') {
            if (typeof value === 'number') {
                valueOf = value.toString();
            } else {
                return null;
            }
        } else {
            valueOf = value;
        }

        lenghtVal = valueOf.length;

        if (lenghtVal < 10 && type === 'F') {
            let aux = '';
            for (let i = 0; i < 10 - valueOf.length; i++) {
                aux += '0';
            }
            valueOf = aux + valueOf;
            lenghtVal = valueOf.length;
        }

        if (lenghtVal < 11 && type === 'C') {
            let aux = '';
            for (let i = 0; i < 11 - valueOf.length; i++) {
                aux += '0';
            }
            valueOf = aux + valueOf;
            lenghtVal = valueOf.length;
        }

        for (let i = lenghtVal - 1; i >= 0; i--) {
            telefone = valueOf[i] + telefone;

            if (i === lenghtVal - 4) {
                telefone = '-' + telefone;
            }

            if (
                type === 'F' && i === lenghtVal - 8 ||
                type === 'C' && i === lenghtVal - 9
            ) {
                telefone = ') ' + telefone;
            }

            if (
                type === 'F' && i === lenghtVal - 10 ||
                type === 'C' && i === lenghtVal - 11
            ) {
                telefone = '(' + telefone;
            }
        }

        return telefone;
    }

}
