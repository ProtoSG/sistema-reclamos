# Sistema de Reclamos - Frontend Administrativo

Frontend administrativo desarrollado con **Vue 3 + TypeScript + Vuetify** para la gestión de reclamos.

## 🚀 Stack Tecnológico

- **Vue 3.5+** - Framework progresivo con Composition API
- **TypeScript 5+** - Tipado estático
- **Vuetify 3.5+** - UI Framework Material Design
- **Vue Router 4+** - Routing con navigation guards
- **Axios** - Cliente HTTP con interceptors JWT
- **Vite 7+** - Build tool ultra-rápido

## ⚙️ Configuración

### Variables de Entorno

**`.env.development`:**
```env
VITE_API_BASE_URL=http://localhost:8080/sistema-reclamos-backend
```

## 🛠️ Comandos Disponibles

```bash
# Desarrollo
npm run dev          # Inicia servidor en http://localhost:3000

# Build
npm run build        # Compila para producción en dist/

# Preview
npm run preview      # Vista previa del build de producción
```

## 🔐 Funcionalidades

### Autenticación
- ✅ Login con JWT
- ✅ Navigation guards
- ✅ Interceptor automático para Bearer token
- ✅ Manejo de 401

### Gestión de Reclamos
- ✅ Lista con búsqueda y filtros
- ✅ Ver detalle completo
- ✅ Cambiar estado
- ✅ Eliminar con confirmación

### Atenciones
- ✅ Timeline cronológico
- ✅ Crear nuevas atenciones

## 🎯 Rutas

- `/login` - Login
- `/reclamos` - Lista de reclamos (protegida)
- `/reclamos/:id` - Detalle (protegida)

## 📦 Estructura

```
src/
├── composables/     # useAuth, useNotification
├── services/        # API services (axios)
├── types/           # TypeScript interfaces
├── views/           # Páginas
├── router/          # Vue Router
└── plugins/         # Vuetify config
```

## 🧪 Testing

1. Asegurar que el backend esté corriendo en `http://localhost:8080`
2. Ejecutar `npm run dev`
3. Acceder a `http://localhost:3000`
4. Login con usuario del backend
5. Probar CRUD de reclamos

---

**Desarrollado con Vue 3 + TypeScript + Vuetify**
