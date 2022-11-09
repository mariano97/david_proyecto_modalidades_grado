import { ITablaContenido } from '@/shared/model/tabla-contenido.model';
import { IProyecto } from '@/shared/model/proyecto.model';

export interface IEstudiante {
  id?: number;
  primerNombre?: string;
  segundoNombre?: string | null;
  apellidos?: string;
  numeroDocumento?: string;
  codigoEstudiantil?: string;
  celular?: string;
  email?: string;
  tipoDocumento?: ITablaContenido;
  proyecto?: IProyecto;
}

export class Estudiante implements IEstudiante {
  constructor(
    public id?: number,
    public primerNombre?: string,
    public segundoNombre?: string | null,
    public apellidos?: string,
    public numeroDocumento?: string,
    public codigoEstudiantil?: string,
    public celular?: string,
    public email?: string,
    public tipoDocumento?: ITablaContenido,
    public proyecto?: IProyecto
  ) {}
}
