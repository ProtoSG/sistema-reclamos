<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12">
        <v-btn
          prepend-icon="mdi-arrow-left"
          @click="$router.back()"
          variant="text"
        >
          Volver
        </v-btn>
      </v-col>
    </v-row>

    <v-row v-if="loading">
      <v-col cols="12" class="text-center">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
      </v-col>
    </v-row>

    <v-row v-else-if="reclamo">
      <!-- Información del Reclamo -->
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="bg-primary text-white">
            <v-icon icon="mdi-file-document" class="mr-2"></v-icon>
            Información del Reclamo #{{ reclamo.id }}
          </v-card-title>

          <v-card-text class="pt-4">
            <!-- Datos del Consumidor -->
            <div class="text-h6 mb-3">Datos del Consumidor</div>
            <v-row dense>
              <v-col cols="6"><strong>Nombres:</strong></v-col>
              <v-col cols="6">{{ reclamo.nombres }}</v-col>

              <v-col cols="6"><strong>Apellidos:</strong></v-col>
              <v-col cols="6">{{ reclamo.apellidos }}</v-col>

              <v-col cols="6"><strong>Documento:</strong></v-col>
              <v-col cols="6">{{ reclamo.tipoDocumento }} - {{ reclamo.numeroDocumento }}</v-col>

              <v-col cols="6"><strong>Email:</strong></v-col>
              <v-col cols="6">{{ reclamo.email }}</v-col>

              <v-col cols="6"><strong>Teléfono:</strong></v-col>
              <v-col cols="6">{{ reclamo.telefono }}</v-col>

              <v-col cols="6"><strong>Dirección:</strong></v-col>
              <v-col cols="6">{{ reclamo.direccion }}</v-col>
            </v-row>

            <v-divider class="my-4"></v-divider>

            <!-- Detalle del Reclamo -->
            <div class="text-h6 mb-3">Detalle del Reclamo</div>
            <v-row dense>
              <v-col cols="6"><strong>Tipo de Bien:</strong></v-col>
              <v-col cols="6">{{ reclamo.tipoBien }}</v-col>

              <v-col cols="6"><strong>Monto:</strong></v-col>
              <v-col cols="6">S/ {{ reclamo.montoReclamado.toFixed(2) }}</v-col>

              <v-col cols="6"><strong>Estado:</strong></v-col>
              <v-col cols="6">
                <v-chip :color="getEstadoColor(reclamo.estado)" size="small">
                  {{ reclamo.estado }}
                </v-chip>
              </v-col>
            </v-row>

            <v-divider class="my-4"></v-divider>

            <div><strong>Descripción:</strong></div>
            <p class="mt-2">{{ reclamo.descripcionReclamo }}</p>

            <div><strong>Pedido del Consumidor:</strong></div>
            <p class="mt-2">{{ reclamo.pedidoConsumidor }}</p>
          </v-card-text>

          <v-card-actions class="px-4 pb-4">
            <v-select
              v-model="nuevoEstado"
              :items="estados"
              label="Cambiar Estado"
              density="compact"
              variant="outlined"
              style="max-width: 200px;"
            ></v-select>
            <v-btn
              color="primary"
              @click="updateEstado"
              :loading="updatingEstado"
            >
              Actualizar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>

      <!-- Atenciones (Timeline) -->
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="bg-secondary text-white">
            <v-icon icon="mdi-timeline-text" class="mr-2"></v-icon>
            Historial de Atenciones
          </v-card-title>

          <v-card-text class="pt-4">
            <!-- Timeline de Atenciones -->
            <v-timeline side="end" density="compact" v-if="atenciones.length > 0">
              <v-timeline-item
                v-for="atencion in atenciones"
                :key="atencion.id"
                dot-color="primary"
                size="small"
              >
                <template v-slot:opposite>
                  <div class="text-caption">{{ formatDate(atencion.fecha) }}</div>
                </template>
                <v-card variant="outlined">
                  <v-card-title class="text-body-1">{{ atencion.titulo }}</v-card-title>
                  <v-card-text>{{ atencion.descripcion }}</v-card-text>
                </v-card>
              </v-timeline-item>
            </v-timeline>

            <v-alert v-else type="info" variant="tonal">
              No hay atenciones registradas para este reclamo.
            </v-alert>
          </v-card-text>

          <v-divider></v-divider>

          <!-- Formulario Nueva Atención -->
          <v-card-text>
            <div class="text-h6 mb-3">Agregar Nueva Atención</div>
            <v-form @submit.prevent="createAtencion">
              <v-text-field
                v-model="nuevaAtencion.titulo"
                label="Título"
                variant="outlined"
                density="comfortable"
                required
              ></v-text-field>

              <v-textarea
                v-model="nuevaAtencion.descripcion"
                label="Descripción"
                variant="outlined"
                rows="3"
                required
                class="mt-4"
              ></v-textarea>

              <v-btn
                type="submit"
                color="secondary"
                :loading="creatingAtencion"
                block
              >
                <v-icon icon="mdi-plus" class="mr-2"></v-icon>
                Agregar Atención
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { reclamoService } from '@/services/reclamoService';
import { atencionService } from '@/services/atencionService';
import { useNotification } from '@/composables/useNotification';
import type { Reclamo, ReclamoEstado } from '@/types/reclamo';
import type { Atencion, CreateAtencionDto } from '@/types/atencion';

const route = useRoute();
const { showSuccess, showError } = useNotification();

const reclamo = ref<Reclamo | null>(null);
const atenciones = ref<Atencion[]>([]);
const loading = ref(false);

const estados: ReclamoEstado[] = ['PENDIENTE', 'EN_PROCESO', 'ATENDIDO', 'CERRADO'];
const nuevoEstado = ref<ReclamoEstado>('PENDIENTE');
const updatingEstado = ref(false);

const nuevaAtencion = ref<CreateAtencionDto>({
  titulo: '',
  descripcion: ''
});
const creatingAtencion = ref(false);

const loadReclamo = async () => {
  loading.value = true;
  try {
    const id = Number(route.params.id);
    reclamo.value = await reclamoService.getById(id);
    nuevoEstado.value = reclamo.value.estado || 'PENDIENTE';
  } catch (err: any) {
    showError('Error al cargar reclamo');
  } finally {
    loading.value = false;
  }
};

const loadAtenciones = async () => {
  try {
    const id = Number(route.params.id);
    atenciones.value = await atencionService.getByReclamoId(id);
  } catch (err: any) {
    showError('Error al cargar atenciones');
  }
};

const updateEstado = async () => {
  if (!reclamo.value) return;

  updatingEstado.value = true;
  try {
    await reclamoService.updateEstado(reclamo.value.id, { reclamoEstado: nuevoEstado.value });
    showSuccess('Estado actualizado exitosamente');
    loadReclamo();
  } catch (err: any) {
    showError('Error al actualizar estado');
  } finally {
    updatingEstado.value = false;
  }
};

const createAtencion = async () => {
  if (!reclamo.value) return;

  creatingAtencion.value = true;
  try {
    await atencionService.create(reclamo.value.id, nuevaAtencion.value);
    showSuccess('Atención agregada exitosamente');
    nuevaAtencion.value = { titulo: '', descripcion: '' };
    loadAtenciones();
  } catch (err: any) {
    showError('Error al agregar atención');
  } finally {
    creatingAtencion.value = false;
  }
};

const getEstadoColor = (estado?: string) => {
  const colors: Record<string, string> = {
    'PENDIENTE': 'orange',
    'EN_PROCESO': 'blue',
    'ATENDIDO': 'green',
    'CERRADO': 'grey'
  };
  return colors[estado || 'PENDIENTE'];
};

const formatDate = (isoDate: string) => {
  return new Date(isoDate).toLocaleString('es-PE');
};

onMounted(() => {
  loadReclamo();
  loadAtenciones();
});
</script>
