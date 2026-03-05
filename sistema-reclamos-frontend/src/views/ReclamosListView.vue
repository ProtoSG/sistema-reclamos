<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title class="d-flex align-center pa-4">
            <v-icon icon="mdi-format-list-bulleted" class="mr-2"></v-icon>
            Gestión de Reclamos

            <v-spacer></v-spacer>

            <!-- Buscador -->
            <v-text-field
              v-model="search"
              append-inner-icon="mdi-magnify"
              label="Buscar"
              single-line
              hide-details
              density="compact"
              style="max-width: 300px;"
              variant="outlined"
            ></v-text-field>
          </v-card-title>

          <v-divider></v-divider>

          <!-- Filtros -->
          <v-card-text>
            <v-row>
              <v-col cols="12" md="3">
                <v-select
                  v-model="estadoFilter"
                  :items="estados"
                  label="Filtrar por Estado"
                  clearable
                  density="compact"
                  variant="outlined"
                ></v-select>
              </v-col>
            </v-row>
          </v-card-text>

          <v-divider></v-divider>

          <!-- Tabla -->
          <v-data-table
            :headers="headers"
            :items="filteredReclamos"
            :search="search"
            :loading="loading"
            class="elevation-0"
            items-per-page="10"
          >
            <!-- Estado con chip coloreado -->
            <template v-slot:item.estado="{ item }">
              <v-chip :color="getEstadoColor(item.estado)" size="small">
                {{ item.estado }}
              </v-chip>
            </template>

            <!-- Monto formateado -->
            <template v-slot:item.montoReclamado="{ item }">
              S/ {{ item.montoReclamado.toFixed(2) }}
            </template>

            <!-- Acciones -->
            <template v-slot:item.actions="{ item }">
              <v-btn
                icon
                size="small"
                @click="viewDetail(item.id)"
                title="Ver Detalle"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>

              <v-btn
                icon
                size="small"
                color="error"
                @click="confirmDelete(item)"
                title="Eliminar"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>

    <!-- Dialog de confirmación de eliminación -->
    <v-dialog v-model="deleteDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5">Confirmar Eliminación</v-card-title>
        <v-card-text>
          ¿Está seguro de eliminar el reclamo de <strong>{{ itemToDelete?.nombres }} {{ itemToDelete?.apellidos }}</strong>?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" @click="deleteDialog = false">Cancelar</v-btn>
          <v-btn color="error" @click="deleteReclamo" :loading="deleting">Eliminar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { reclamoService } from '@/services/reclamoService';
import { useNotification } from '@/composables/useNotification';
import type { Reclamo } from '@/types/reclamo';

const router = useRouter();
const { showSuccess, showError } = useNotification();

const reclamos = ref<Reclamo[]>([]);
const loading = ref(false);
const search = ref('');
const estadoFilter = ref<string | null>(null);

const estados = ['PENDIENTE', 'EN_PROCESO', 'ATENDIDO', 'CERRADO'];

const headers = [
  { title: 'ID', key: 'id', sortable: true },
  { title: 'Nombres', key: 'nombres', sortable: true },
  { title: 'Apellidos', key: 'apellidos', sortable: true },
  { title: 'Email', key: 'email', sortable: true },
  { title: 'Estado', key: 'estado', sortable: true },
  { title: 'Monto', key: 'montoReclamado', sortable: true },
  { title: 'Acciones', key: 'actions', sortable: false }
];

// Filtrado combinado (búsqueda + estado)
const filteredReclamos = computed(() => {
  let result = reclamos.value;

  if (estadoFilter.value) {
    result = result.filter(r => r.estado === estadoFilter.value);
  }

  return result;
});

const deleteDialog = ref(false);
const itemToDelete = ref<Reclamo | null>(null);
const deleting = ref(false);

const loadReclamos = async () => {
  loading.value = true;
  try {
    reclamos.value = await reclamoService.getAll();
  } catch (err: any) {
    showError('Error al cargar reclamos');
  } finally {
    loading.value = false;
  }
};

const viewDetail = (id: number) => {
  router.push(`/reclamos/${id}`);
};

const confirmDelete = (item: Reclamo) => {
  itemToDelete.value = item;
  deleteDialog.value = true;
};

const deleteReclamo = async () => {
  if (!itemToDelete.value) return;

  deleting.value = true;
  try {
    await reclamoService.delete(itemToDelete.value.id);
    showSuccess('Reclamo eliminado exitosamente');
    deleteDialog.value = false;
    loadReclamos();
  } catch (err: any) {
    showError('Error al eliminar reclamo');
  } finally {
    deleting.value = false;
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

onMounted(() => {
  loadReclamos();
});
</script>
