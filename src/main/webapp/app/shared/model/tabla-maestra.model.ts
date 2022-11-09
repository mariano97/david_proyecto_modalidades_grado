export interface ITablaMaestra {
  id?: number;
  nombre?: string;
  codigo?: string;
}

export class TablaMaestra implements ITablaMaestra {
  constructor(public id?: number, public nombre?: string, public codigo?: string) {}
}
