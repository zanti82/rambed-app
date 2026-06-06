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

// Registra un abono parcial a una factura
function registrarAbono(datos) {
  return api.post('/facturas/abonos', datos);
}

// Obtiene todos los abonos de una factura
function listarAbonos(facturaId) {
  return api.get('/facturas/' + facturaId + '/abonos');
}

// Obtiene comisiones, con filtro opcional por liquidada (0 o 1)
function listarComisiones(liquidada) {
  if (liquidada !== undefined && liquidada !== null) {
    return api.get('/facturas/comisiones?liquidada=' + liquidada);
  }
  return api.get('/facturas/comisiones');
}

// Liquida todas las comisiones pendientes de un vendedor
function liquidarComisiones(vendedorId) {
  return api.patch('/facturas/comisiones/liquidar/' + vendedorId);
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
  eliminar,
  registrarAbono,
  listarAbonos,
  listarComisiones,
  liquidarComisiones
};