import api from './api';
import type { Reclamo, CreateReclamoDto, UpdateEstadoDto } from '@/types/reclamo';

export const reclamoService = {
  async getAll(): Promise<Reclamo[]> {
    const { data } = await api.get<Reclamo[]>('/api/reclamos');
    return data;
  },

  async getById(id: number): Promise<Reclamo> {
    const { data } = await api.get<Reclamo>(`/api/reclamos/${id}`);
    return data;
  },

  async create(reclamo: CreateReclamoDto): Promise<Reclamo> {
    const { data } = await api.post<Reclamo>('/api/reclamos', reclamo);
    return data;
  },

  async updateEstado(id: number, payload: UpdateEstadoDto): Promise<void> {
    await api.put(`/api/reclamos/${id}/estado`, payload);
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/api/reclamos/${id}`);
  }
};
