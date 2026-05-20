// Importa la instancia de axios configurada
import api from './axios';

// Obtiene todo el inventario
function listarTodo() {
  return api.get('/inventario');
}

// Obtiene inventario de una referencia especifica
function listarPorReferencia(referenciaId) {
  return api.get('/inventario/referencia/' + referenciaId);
}

// Obtiene solo registros con stock disponible
function listarConStock() {
  return api.get('/inventario/con-stock');
}

// Obtiene un registro por su ID
function buscarPorId(id) {
  return api.get('/inventario/' + id);
}

// Obtiene un registro por referencia y talla
function buscarPorReferenciaYTalla(referenciaId, talla) {
  return api.get('/inventario/referencia/' + referenciaId + '/talla/' + talla);
}

// Crea un registro nuevo de inventario
function guardar(datos) {
  return api.post('/inventario', datos);
}

// Actualiza cantidad y precio de un registro
function actualizar(id, datos) {
  return api.put('/inventario/' + id, datos);
}

// Elimina un registro solo en desarrollo
function eliminar(id) {
  return api.delete('/inventario/' + id);
}

export default {
  listarTodo,
  listarPorReferencia,
  listarConStock,
  buscarPorId,
  buscarPorReferenciaYTalla,
  guardar,
  actualizar,
  eliminar
};