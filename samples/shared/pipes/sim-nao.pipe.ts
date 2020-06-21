import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'simNao'
})
export class SimNaoPipe implements PipeTransform {

    transform(value: string): string {
        switch (value) {
            case 'S':
                return 'SIM';
                break;

            case 'N':
                return 'NÃO';
                break;

            default:
                return null;
                break;
        }

        return null;
    }

}
