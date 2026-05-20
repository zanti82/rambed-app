// Importa la instancia de axios configurada
import api from './axios';

// Obtiene todas las facturas
function listarTodas() {
  return api.get('/facturas');
}

// Obtiene facturas filtradas por estado
function listarPorEstado(estado) {
  return api.get('/facturas/estado/' + estado);
}

// Obtiene facturas de un cliente especifico
function listarPorCliente(clienteId) {
  return api.get('/facturas/cliente/' + clienteId);
}

// Obtiene facturas de un vendedor especifico
function listarPorVendedor(vendedorId) {
  return api.get('/facturas/vendedor/' + vendedorId);
}

// Obtiene una factura por su ID
function buscarPorId(id) {
  return api.get('/facturas/' + id);
}

// Crea una factura nueva
function guardar(datos) {
  return api.post('/facturas', datos);
}

// Registra el pago de una factura
function registrarPago(id, pagoRequest) {
  return api.patch('/facturas/' + id + '/pagar', pagoRequest);
}

// Anula una factura
function anular(id) {
  return api.patch('/facturas/' + id + '/anular');
}

// Elimina una factura solo en desarrollo
function eliminar(id) {
  return api.delete('/facturas/' + id);
}

export default {
  listarTodas,
  listarPorEstado,
  listarPorCliente,
  listarPorVendedor,
  buscarPorId,
  guardar,
  registrarPago,
  anular,
  eliminar
};