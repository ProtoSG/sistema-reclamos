import api from './api';
import type { Atencion, CreateAtencionDto } from '@/types/atencion';

export const atencionService = {
  async getByReclamoId(reclamoId: number): Promise<Atencion[]> {
    const { data } = await api.get<Atencion[]>(`/api/reclamos/${reclamoId}/atenciones`);
    return data;
  },

  async create(reclamoId: number, atencion: CreateAtencionDto): Promise<Atencion> {
    const { data } = await api.post<Atencion>(`/api/reclamos/${reclamoId}/atenciones`, atencion);
    return data;
  }
};
