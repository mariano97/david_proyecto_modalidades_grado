import { IProyecto } from '@/shared/model/proyecto.model';

export interface IObservaciones {
  id?: number;
  observacion?: string;
  proyecto?: IProyecto;
}

export class Observaciones implements IObservaciones {
  constructor(public id?: number, public observacion?: string, public proyecto?: IProyecto) {}
}
