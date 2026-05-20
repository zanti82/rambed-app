// Importa la instancia de axios configurada
import api from './axios';

// Obtiene todas las referencias
function listarTodas() {
  return api.get('/referencias');
}

// Obtiene solo las referencias activas
function listarActivas() {
  return api.get('/referencias/activas');
}

// Obtiene referencias por marca
function listarPorMarca(marca) {
  return api.get('/referencias/marca/' + marca);
}

// Obtiene referencias activas por marca
function listarActivasPorMarca(marca) {
  return api.get('/referencias/activas/marca/' + marca);
}

// Obtiene una referencia por su ID
function buscarPorId(id) {
  return api.get('/referencias/' + id);
}

// Crea una referencia nueva
function guardar(datos) {
  return api.post('/referencias', datos);
}

// Actualiza una referencia existente
function actualizar(id, datos) {
  return api.put('/referencias/' + id, datos);
}

// Activa una referencia
function activar(id) {
  return api.patch('/referencias/' + id + '/activar');
}

// Desactiva una referencia
function desactivar(id) {
  return api.patch('/referencias/' + id + '/desactivar');
}

// Elimina una referencia solo en desarrollo
function eliminar(id) {
  return api.delete('/referencias/' + id);
}

export default {
  listarTodas,
  listarActivas,
  listarPorMarca,
  listarActivasPorMarca,
  buscarPorId,
  guardar,
  actualizar,
  activar,
  desactivar,
  eliminar
};