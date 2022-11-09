export interface IEmpresa {
  id?: number;
  nombre?: string;
  nit?: string;
  telefono?: string | null;
  email?: string;
}

export class Empresa implements IEmpresa {
  constructor(public id?: number, public nombre?: string, public nit?: string, public telefono?: string | null, public email?: string) {}
}
