package com.reclamos.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/public/reclamo-form")
public class PublicFormServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html; charset=UTF-8");
    
    PrintWriter out = resp.getWriter();
    
    String html = """
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Libro de Reclamaciones</title>
  
  <!-- Vuetify CSS -->
  <link href="https://cdn.jsdelivr.net/npm/vuetify@3.5.7/dist/vuetify.min.css" rel="stylesheet">
  
  <!-- Material Design Icons -->
  <link href="https://cdn.jsdelivr.net/npm/@mdi/font@7.4.47/css/materialdesignicons.min.css" rel="stylesheet">
  
  <style>
    body {
      margin: 0;
      font-family: 'Roboto', sans-serif;
    }
  </style>
</head>
<body>
  <div id="app">
    <v-app>
      <!-- App Bar -->
      <v-app-bar color="primary" prominent>
        <v-app-bar-title class="text-h5 font-weight-bold">
          <v-icon icon="mdi-file-document-edit" class="mr-2"></v-icon>
          Libro de Reclamaciones
        </v-app-bar-title>
      </v-app-bar>

      <!-- Main Content -->
      <v-main>
        <v-container class="py-8" style="max-width: 900px;">
          
          <!-- Información Introductoria -->
          <v-alert type="info" variant="tonal" class="mb-6">
            <v-alert-title class="font-weight-bold">Información</v-alert-title>
            Complete este formulario para registrar su reclamo. Todos los campos son obligatorios.
            Recibirá un número de reclamo para realizar el seguimiento.
          </v-alert>

          <!-- Formulario Principal -->
          <v-card>
            <v-card-title class="bg-primary text-white">
              <span class="text-h6">Datos del Consumidor y Reclamo</span>
            </v-card-title>

            <v-card-text class="pt-6">
              <v-form ref="form" @submit.prevent="submitForm">
                
                <!-- Sección: Datos del Consumidor -->
                <div class="text-h6 mb-4 text-primary">
                  <v-icon icon="mdi-account" class="mr-2"></v-icon>
                  Datos del Consumidor
                </div>

                <v-row>
                  <v-col cols="12" md="4">
                    <v-select
                      v-model="form.tipoDocumento"
                      :items="tiposDocumento"
                      label="Tipo de Documento *"
                      :rules="[rules.required]"
                      variant="outlined"
                      density="comfortable"
                    ></v-select>
                  </v-col>

                  <v-col cols="12" md="8">
                    <v-text-field
                      v-model="form.numeroDocumento"
                      label="Número de Documento *"
                      :rules="[rules.required, rules.documento]"
                      variant="outlined"
                      density="comfortable"
                      hint="Ingrese el número sin espacios ni guiones"
                      persistent-hint
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="form.nombres"
                      label="Nombres *"
                      :rules="[rules.required]"
                      variant="outlined"
                      density="comfortable"
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="form.apellidos"
                      label="Apellidos *"
                      :rules="[rules.required]"
                      variant="outlined"
                      density="comfortable"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12">
                    <v-text-field
                      v-model="form.direccion"
                      label="Dirección *"
                      :rules="[rules.required]"
                      variant="outlined"
                      density="comfortable"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="form.telefono"
                      label="Teléfono *"
                      type="tel"
                      :rules="[rules.required, rules.telefono]"
                      variant="outlined"
                      density="comfortable"
                      hint="Ingrese un número válido"
                      persistent-hint
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="form.email"
                      label="Correo Electrónico *"
                      type="email"
                      :rules="[rules.required, rules.email]"
                      variant="outlined"
                      density="comfortable"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-divider class="my-6"></v-divider>

                <!-- Sección: Detalle del Reclamo -->
                <div class="text-h6 mb-4 text-primary">
                  <v-icon icon="mdi-clipboard-text" class="mr-2"></v-icon>
                  Detalle del Reclamo
                </div>

                <v-row>
                  <v-col cols="12" md="6">
                    <v-select
                      v-model="form.tipoBien"
                      :items="tiposBien"
                      label="Tipo de Bien *"
                      :rules="[rules.required]"
                      variant="outlined"
                      density="comfortable"
                    ></v-select>
                  </v-col>

                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model.number="form.montoReclamado"
                      label="Monto Reclamado *"
                      type="number"
                      step="0.01"
                      min="0"
                      :rules="[rules.required, rules.monto]"
                      variant="outlined"
                      density="comfortable"
                      prefix="S/"
                      hint="Ingrese el monto en soles"
                      persistent-hint
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12">
                    <v-textarea
                      v-model="form.descripcionReclamo"
                      label="Descripción del Reclamo *"
                      :rules="[rules.required, rules.minLength(10)]"
                      variant="outlined"
                      rows="4"
                      counter
                      hint="Describa detalladamente su reclamo"
                      persistent-hint
                    ></v-textarea>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12">
                    <v-textarea
                      v-model="form.pedidoConsumidor"
                      label="Pedido del Consumidor *"
                      :rules="[rules.required, rules.minLength(10)]"
                      variant="outlined"
                      rows="4"
                      counter
                      hint="Indique qué solución espera para su reclamo"
                      persistent-hint
                    ></v-textarea>
                  </v-col>
                </v-row>

                <!-- Alertas de Éxito/Error -->
                <v-alert
                  v-if="success"
                  type="success"
                  variant="tonal"
                  closable
                  class="mt-4"
                  @click:close="success = false"
                >
                  <v-alert-title class="font-weight-bold">¡Reclamo Registrado Exitosamente!</v-alert-title>
                  <div class="mt-2">
                    Su número de reclamo es: <strong class="text-h6">#{{ reclamoId }}</strong>
                  </div>
                  <div class="mt-2">
                    Por favor, guarde este número para dar seguimiento a su reclamo.
                  </div>
                </v-alert>

                <v-alert
                  v-if="error"
                  type="error"
                  variant="tonal"
                  closable
                  class="mt-4"
                  @click:close="error = null"
                >
                  <v-alert-title class="font-weight-bold">Error al Procesar</v-alert-title>
                  <div class="mt-2">{{ error }}</div>
                </v-alert>

                <!-- Botones de Acción -->
                <v-card-actions class="px-0 pt-6">
                  <v-spacer></v-spacer>
                  <v-btn
                    v-if="success"
                    color="secondary"
                    variant="outlined"
                    @click="resetForm"
                    size="large"
                  >
                    <v-icon icon="mdi-plus" class="mr-2"></v-icon>
                    Nuevo Reclamo
                  </v-btn>
                  <v-btn
                    type="submit"
                    color="primary"
                    :loading="loading"
                    :disabled="loading"
                    size="large"
                    class="px-8"
                  >
                    <v-icon icon="mdi-send" class="mr-2"></v-icon>
                    Enviar Reclamo
                  </v-btn>
                </v-card-actions>

              </v-form>
            </v-card-text>
          </v-card>

          <!-- Footer Informativo -->
          <v-card class="mt-6" variant="outlined">
            <v-card-text class="text-center text-body-2 text-grey-darken-1">
              <v-icon icon="mdi-shield-check" class="mr-2"></v-icon>
              Sus datos personales serán tratados conforme a la Ley de Protección de Datos Personales.
              <br>
              Para consultas, comuníquese con nuestro servicio al cliente.
            </v-card-text>
          </v-card>

        </v-container>
      </v-main>
    </v-app>
  </div>

  <!-- Vue 3 CDN -->
  <script src="https://cdn.jsdelivr.net/npm/vue@3.4.21/dist/vue.global.prod.js"></script>
  
  <!-- Vuetify CDN -->
  <script src="https://cdn.jsdelivr.net/npm/vuetify@3.5.7/dist/vuetify.min.js"></script>
  
  <!-- Vue Application -->
  <script>
    const { createApp } = Vue;
    const { createVuetify } = Vuetify;

    // Crear instancia de Vuetify
    const vuetify = createVuetify({
      theme: {
        defaultTheme: 'light',
        themes: {
          light: {
            colors: {
              primary: '#1976D2',
              secondary: '#424242',
              accent: '#82B1FF',
              error: '#FF5252',
              info: '#2196F3',
              success: '#4CAF50',
              warning: '#FFC107',
            },
          },
        },
      },
    });

    // Crear aplicación Vue
    const app = createApp({
      data() {
        return {
          // Modelo del formulario
          form: {
            tipoDocumento: '',
            numeroDocumento: '',
            nombres: '',
            apellidos: '',
            direccion: '',
            telefono: '',
            email: '',
            tipoBien: '',
            montoReclamado: null,
            descripcionReclamo: '',
            pedidoConsumidor: ''
          },
          
          // Opciones para selects
          tiposDocumento: ['DNI', 'CE', 'Pasaporte'],
          tiposBien: ['Producto', 'Servicio'],
          
          // Estados de la UI
          loading: false,
          success: false,
          error: null,
          reclamoId: null,
          
          // Reglas de validación
          rules: {
            required: v => !!v || 'Este campo es obligatorio',
            email: v => /.+@.+\\..+/.test(v) || 'Correo electrónico inválido',
            documento: v => {
              if (!v) return 'Este campo es obligatorio';
              if (v.length < 8) return 'Número de documento inválido';
              return true;
            },
            telefono: v => {
              if (!v) return 'Este campo es obligatorio';
              if (v.length < 7) return 'Número de teléfono inválido';
              return true;
            },
            monto: v => {
              if (v === null || v === undefined || v === '') return 'Este campo es obligatorio';
              if (v <= 0) return 'El monto debe ser mayor a 0';
              return true;
            },
            minLength: (min) => {
              return v => {
                if (!v) return 'Este campo es obligatorio';
                if (v.length < min) return `Debe tener al menos ${min} caracteres`;
                return true;
              };
            }
          }
        };
      },
      
      methods: {
        async submitForm() {
          // Validar formulario
          const { valid } = await this.$refs.form.validate();
          
          if (!valid) {
            this.error = 'Por favor, complete todos los campos correctamente';
            return;
          }
          
          this.loading = true;
          this.error = null;
          this.success = false;
          
          try {
            // Obtener el context path dinámicamente
            const contextPath = window.location.pathname.split('/').slice(0, -2).join('/');
            const apiUrl = contextPath + '/api/reclamos';
            
            const response = await fetch(apiUrl, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(this.form)
            });
            
            if (!response.ok) {
              const errorData = await response.json();
              throw new Error(errorData.error || 'Error al procesar el reclamo');
            }
            
            const data = await response.json();
            this.success = true;
            this.reclamoId = data.id;
            
            // Scroll hacia arriba para mostrar el mensaje de éxito
            window.scrollTo({ top: 0, behavior: 'smooth' });
            
          } catch (err) {
            this.error = err.message || 'Error de conexión. Por favor, intente nuevamente.';
            console.error('Error:', err);
          } finally {
            this.loading = false;
          }
        },
        
        resetForm() {
          // Resetear formulario
          this.form = {
            tipoDocumento: '',
            numeroDocumento: '',
            nombres: '',
            apellidos: '',
            direccion: '',
            telefono: '',
            email: '',
            tipoBien: '',
            montoReclamado: null,
            descripcionReclamo: '',
            pedidoConsumidor: ''
          };
          
          // Resetear validaciones
          this.$refs.form.reset();
          
          // Limpiar estados
          this.success = false;
          this.error = null;
          this.reclamoId = null;
          
          // Scroll hacia arriba
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      }
    });

    // Montar la aplicación
    app.use(vuetify).mount('#app');
  </script>
</body>
</html>
    """;
    
    out.print(html);
  }
}
