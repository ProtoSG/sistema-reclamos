import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/reclamos'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/reclamos',
      name: 'ReclamosList',
      component: () => import('@/views/ReclamosListView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/reclamos/:id',
      name: 'ReclamoDetail',
      component: () => import('@/views/ReclamoDetailView.vue'),
      meta: { requiresAuth: true },
      props: true
    }
  ]
});

// Navigation guard para proteger rutas
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('accessToken');
  const requiresAuth = to.meta.requiresAuth;

  if (requiresAuth && !token) {
    next('/login');
  } else if (to.path === '/login' && token) {
    next('/reclamos');
  } else {
    next();
  }
});

export default router;
