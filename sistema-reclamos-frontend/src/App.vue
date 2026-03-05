<template>
  <v-app>
    <!-- Barra de navegación (solo si autenticado) -->
    <v-app-bar v-if="isAuthenticated" color="primary" prominent>
      <v-app-bar-title class="font-weight-bold">
        <v-icon icon="mdi-file-document-multiple" class="mr-2"></v-icon>
        Sistema de Reclamos - Admin
      </v-app-bar-title>

      <v-spacer></v-spacer>

      <v-btn icon @click="logout">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>

    <!-- Contenido principal -->
    <v-main>
      <router-view />
    </v-main>

    <!-- Snackbar global para notificaciones -->
    <v-snackbar
      v-model="snackbar"
      :color="color"
      :timeout="3000"
      location="top"
    >
      {{ message }}
    </v-snackbar>
  </v-app>
</template>

<script setup lang="ts">
import { useAuth } from '@/composables/useAuth';
import { useNotification } from '@/composables/useNotification';

const { isAuthenticated, logout } = useAuth();
const { snackbar, message, color } = useNotification();
</script>
