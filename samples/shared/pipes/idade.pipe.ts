import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'idade'
})
export class IdadePipe implements PipeTransform {

    transform(value: Date): number {
        const dataNascimento = new Date(value);
        const today = new Date();
        let idade = today.getFullYear() - dataNascimento.getFullYear();

        if (today.getMonth() < dataNascimento.getMonth() ||
            (today.getMonth() === dataNascimento.getMonth() &&
            today.getDate() < dataNascimento.getDate())) {
            idade--;
        }
        return idade === 0 ? 0 : idade;
    }

}
