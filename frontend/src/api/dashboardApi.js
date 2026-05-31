import api from './axios';

// Obtiene los datos del dashboard segun el rol del usuario
function obtenerDashboard() {
  return api.get('/dashboard');
}

export default { obtenerDashboard };