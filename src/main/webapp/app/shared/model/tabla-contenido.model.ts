import { ITablaMaestra } from '@/shared/model/tabla-maestra.model';

export interface ITablaContenido {
  id?: number;
  nombre?: string;
  codigo?: string;
  tablaMaestra?: ITablaMaestra;
}

export class TablaContenido implements ITablaContenido {
  constructor(public id?: number, public nombre?: string, public codigo?: string, public tablaMaestra?: ITablaMaestra) {}
}
