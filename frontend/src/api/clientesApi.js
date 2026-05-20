// Importa la instancia de axios configurada
import api from './axios';

// Obtiene todos los clientes
function listarTodos() {
  return api.get('/clientes');
}

// Obtiene solo los clientes activos
function listarActivos() {
  return api.get('/clientes/activos');
}

// Obtiene clientes por ciudad
function listarPorCiudad(ciudad) {
  return api.get('/clientes/ciudad/' + ciudad);
}

// Obtiene clientes de un vendedor especifico
function listarPorVendedor(vendedorId) {
  return api.get('/clientes/vendedor/' + vendedorId);
}

// Obtiene un cliente por su ID
function buscarPorId(id) {
  return api.get('/clientes/' + id);
}

// Crea un cliente nuevo
function guardar(datos) {
  return api.post('/clientes', datos);
}

// Actualiza un cliente existente
function actualizar(id, datos) {
  return api.put('/clientes/' + id, datos);
}

// Activa un cliente
function activar(id) {
  return api.patch('/clientes/' + id + '/activar');
}

// Desactiva un cliente
function desactivar(id) {
  return api.patch('/clientes/' + id + '/desactivar');
}

// Elimina un cliente solo en desarrollo
function eliminar(id) {
  return api.delete('/clientes/' + id);
}

export default {
  listarTodos,
  listarActivos,
  listarPorCiudad,
  listarPorVendedor,
  buscarPorId,
  guardar,
  actualizar,
  activar,
  desactivar,
  eliminar
};