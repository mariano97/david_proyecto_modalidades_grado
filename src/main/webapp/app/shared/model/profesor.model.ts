export interface IProfesor {
  id?: number;
  primerNombre?: string;
  segundoNombre?: string | null;
  apellidos?: string;
  email?: string;
  telefono?: string | null;
}

export class Profesor implements IProfesor {
  constructor(
    public id?: number,
    public primerNombre?: string,
    public segundoNombre?: string | null,
    public apellidos?: string,
    public email?: string,
    public telefono?: string | null
  ) {}
}
