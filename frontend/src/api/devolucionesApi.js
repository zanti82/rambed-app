// Importa la instancia de axios configurada
import api from './axios';

// Obtiene todas las devoluciones
function listarTodas() {
  return api.get('/devoluciones');
}

// Obtiene devoluciones de un item de factura especifico
function listarPorFacturaDetalle(facturaDetalleId) {
  return api.get('/devoluciones/factura-detalle/' + facturaDetalleId);
}

// Registra una devolucion nueva
function registrar(datos) {
  return api.post('/devoluciones', datos);
}

export default {
  listarTodas,
  listarPorFacturaDetalle,
  registrar
};