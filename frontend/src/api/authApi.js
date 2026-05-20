// Importa la instancia de axios configurada
import api from './axios';

// Envia las credenciales al backend y recibe el token JWT
function login(username, password) {
  // Crea el objeto con las credenciales
  const credenciales = {
    username: username,
    password: password
  };

  // Hace la peticion POST al endpoint de login
  return api.post('/auth/login', credenciales);
}

// Exporta las funciones de autenticacion
export default { login };