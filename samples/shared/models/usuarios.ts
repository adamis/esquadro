import { BaseResourceModel } from './../../shared/models/base-resource.model';

export class Usuarios extends BaseResourceModel {

  constructor(
    public id?: number,
    public nome?: string,
    public usuario?: string,
    public senha?: string,
    public foto?: string,
  ) {
    super();
  }
  static fromJson(jsonData: any): Usuarios {
    return Object.assign(new Usuarios(), jsonData);
  }
}
