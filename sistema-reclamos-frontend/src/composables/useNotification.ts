import { ref } from 'vue';

const snackbar = ref(false);
const message = ref('');
const color = ref('success');

export function useNotification() {
  const showSuccess = (msg: string) => {
    message.value = msg;
    color.value = 'success';
    snackbar.value = true;
  };

  const showError = (msg: string) => {
    message.value = msg;
    color.value = 'error';
    snackbar.value = true;
  };

  const showInfo = (msg: string) => {
    message.value = msg;
    color.value = 'info';
    snackbar.value = true;
  };

  return {
    snackbar,
    message,
    color,
    showSuccess,
    showError,
    showInfo
  };
}
