// Importa la instancia de axios configurada
import api from './axios';

// Obtiene todos los vendedores
function listarTodos() {
  return api.get('/vendedores');
}

// Obtiene solo los vendedores activos
function listarActivos() {
  return api.get('/vendedores/activos');
}

// Obtiene un vendedor por su ID
function buscarPorId(id) {
  return api.get('/vendedores/' + id);
}

// Crea un vendedor nuevo
function guardar(datos) {
  return api.post('/vendedores', datos);
}

// Actualiza un vendedor existente
function actualizar(id, datos) {
  return api.put('/vendedores/' + id, datos);
}

// Activa un vendedor
function activar(id) {
  return api.patch('/vendedores/' + id + '/activar');
}

// Desactiva un vendedor
function desactivar(id) {
  return api.patch('/vendedores/' + id + '/desactivar');
}

// Elimina un vendedor solo en desarrollo
function eliminar(id) {
  return api.delete('/vendedores/' + id);
}

export default {
  listarTodos,
  listarActivos,
  buscarPorId,
  guardar,
  actualizar,
  activar,
  desactivar,
  eliminar
};