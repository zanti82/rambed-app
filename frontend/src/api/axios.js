// Importa axios para hacer peticiones HTTP
import axios from 'axios';

// Crea una instancia de axios con la URL base del backend
const api = axios.create({
  baseURL: 'https://rambed-app.vercel.app',
});

// Interceptor que agrega el token JWT a cada peticion automaticamente
api.interceptors.request.use(function(config) {
  // Obtiene el token guardado en localStorage
  const token = localStorage.getItem('token');

  // Si hay token lo agrega al header Authorization
  if (token) {
    config.headers.Authorization = 'Bearer ' + token;
  }

  return config;
});

// Interceptor que maneja errores de autenticacion globalmente
api.interceptors.response.use(
  function(response) {
    // Si la respuesta es exitosa la retorna sin cambios
    return response;
  },
  function(error) {
    // Si el servidor responde con 401 el token expiro o es invalido
    if (error.response && error.response.status === 401) {
      // Limpia el localStorage y redirige al login
      localStorage.removeItem('token');
      localStorage.removeItem('usuario');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Exporta la instancia para usarla en toda la app
export default api;