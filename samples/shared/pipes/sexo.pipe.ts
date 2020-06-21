import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'sexo'
})
export class SexoPipe implements PipeTransform {

    transform(value: string): string {
        switch (value) {
            case 'F':
                return 'FEMININO';
                break;

            case 'M':
                return 'MASCULINO';
                break;

            default:
                return null;
                break;
        }

        return null;
    }

}
