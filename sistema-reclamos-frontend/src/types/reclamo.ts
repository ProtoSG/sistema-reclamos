export interface Reclamo {
  id: number;
  tipoDocumento: string;
  numeroDocumento: string;
  nombres: string;
  apellidos: string;
  direccion: string;
  telefono: string;
  email: string;
  tipoBien: string;
  montoReclamado: number;
  descripcionReclamo: string;
  pedidoConsumidor: string;
  estado?: ReclamoEstado;
  fechaRegistro?: string;
  fechaCierre?: string | null;
}

export type ReclamoEstado = 'PENDIENTE' | 'EN_PROCESO' | 'ATENDIDO' | 'CERRADO';

export interface CreateReclamoDto {
  tipoDocumento: string;
  numeroDocumento: string;
  nombres: string;
  apellidos: string;
  direccion: string;
  telefono: string;
  email: string;
  tipoBien: string;
  montoReclamado: number;
  descripcionReclamo: string;
  pedidoConsumidor: string;
}

export interface UpdateEstadoDto {
  reclamoEstado: ReclamoEstado;
}
