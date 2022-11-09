import { ITablaContenido } from '@/shared/model/tabla-contenido.model';
import { IEmpresa } from '@/shared/model/empresa.model';
import { IArl } from '@/shared/model/arl.model';

export interface IProyecto {
  id?: number;
  nombre?: string;
  acta?: boolean;
  fechaInicio?: Date;
  fechaTermino?: Date | null;
  tipoModalidad?: ITablaContenido;
  empresa?: IEmpresa | null;
  arl?: IArl | null;
}

export class Proyecto implements IProyecto {
  constructor(
    public id?: number,
    public nombre?: string,
    public acta?: boolean,
    public fechaInicio?: Date,
    public fechaTermino?: Date | null,
    public tipoModalidad?: ITablaContenido,
    public empresa?: IEmpresa | null,
    public arl?: IArl | null
  ) {
    this.acta = this.acta ?? false;
  }
}
