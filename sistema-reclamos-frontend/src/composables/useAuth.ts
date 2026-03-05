import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '@/services/authService';
import type { LoginCredentials, Usuario } from '@/types/auth';

const currentUser = ref<Usuario | null>(null);
const accessToken = ref<string | null>(localStorage.getItem('accessToken'));

export function useAuth() {
  const router = useRouter();

  const isAuthenticated = computed(() => !!accessToken.value);

  const login = async (credentials: LoginCredentials) => {
    const response = await authService.login(credentials);
    
    accessToken.value = response.accessToken;
    currentUser.value = response.usuario;
    
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
    
    router.push('/reclamos');
  };

  const logout = () => {
    accessToken.value = null;
    currentUser.value = null;
    
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    
    router.push('/login');
  };

  const checkAuth = () => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      router.push('/login');
      return false;
    }
    accessToken.value = token;
    return true;
  };

  return {
    currentUser,
    isAuthenticated,
    login,
    logout,
    checkAuth
  };
}
