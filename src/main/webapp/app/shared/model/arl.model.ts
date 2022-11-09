export interface IArl {
  id?: number;
  nombre?: string;
}

export class Arl implements IArl {
  constructor(public id?: number, public nombre?: string) {}
}
