export interface Atencion {
  id: number;
  fecha: string; // ISO date string
  titulo: string;
  descripcion: string;
}

export interface CreateAtencionDto {
  titulo: string;
  descripcion: string;
}
