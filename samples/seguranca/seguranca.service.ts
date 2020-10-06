import { HttpParams, HttpHeaders } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Usuarios } from './../shared/models/usuarios';
import { Injectable, Injector } from '@angular/core';
import { BaseResourceService } from '../shared/services/base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class SegurancaService extends BaseResourceService<Usuarios> {

  constructor(protected injector: Injector) {
    super(environment.apiUrl + 'login', injector, Usuarios.fromJson);
  }

 header = new HttpHeaders(
    {
      Authorization: 'Basic ' + btoa(''),
      'Content-Type': 'application/json'
    });

   /** MÉTODO POST DE LOGIN, PARÂMETROS E HEADER SÃO DIFERENTES.
   *  NO HEADER VOCÊ TEM QUE ADICIONAR O CONTENT-TYPE: APPLICATION/JSON
   *  OS PARÂMETROS SÃO PASSADOS APÓS A URL NUM JSON.STRINGIFY
   *  E O HEADER É PASSADO SOZINHO, TUDO COMO ESTÁ ABAIXO
   */

  login(usuario: string, senhaP: string, empresa: any, sistema: any): Promise<any> {
    const param = { usuario, senhaP, empresa, sistema };

    //ATE AQUI
    return this.http.post(environment.apiUrl + 'login', JSON.stringify(param),
      { headers: this.header }
    )
      .toPromise()
      .then(response => {
        return response;
      });

  }

  trocarSenha(usuario: string, senhaP: string, novaSenhaP: string, repitaSenhaP: string): Promise<any> {

    const requestJson = {
      cpfCnpj: usuario,
      senha: senhaP,
      novaSenha: novaSenhaP,
      repitaSenha: repitaSenhaP
    };
	
		return this.http.put(
		  environment.apiUrl + 'troca-senha',
		  JSON.stringify(requestJson),
		  {
			headers: this.header.append('Content-Type', 'application/json')
		  }
		)
		  .toPromise()
		  .then(response => {
			return response;
		  });	
  }

  recuperarSenhaEmail(email: string): Promise<any> {
    const param = new HttpParams();

    return this.http.get<any>(
      environment.apiUrl + 'recuperarSenha/email/' + email,
      { headers: this.header, params: param }
    )
      .toPromise()
      .then(response => {
        return response;
      });
  }

  recuperarSenhacpfCnpj(cpfCnpj: string): Promise<any> {
    const param = new HttpParams();

    return this.http.get<any>(
      environment.apiUrl + 'recuperarSenha/cpfCnpj/' + cpfCnpj,
      { headers: this.header, params: param }
    )
      .toPromise()
      .then(response => {
        return response;
      });
  }


}
