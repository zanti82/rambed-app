// Importa la instancia de axios configurada
import api from './axios';

// Obtiene los items de una factura especifica
function listarPorFactura(facturaId) {
  return api.get('/factura-detalle/factura/' + facturaId);
}

// Agrega un item a una factura
function agregar(datos) {
  return api.post('/factura-detalle', datos);
}

// Elimina un item de una factura
function eliminar(id) {
  return api.delete('/factura-detalle/' + id);
}

export default {
  listarPorFactura,
  agregar,
  eliminar
};