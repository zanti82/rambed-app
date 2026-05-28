import api from './axios';

function listarTodos() {
  return api.get('/usuarios');
}

function guardar(datos) {
  return api.post('/usuarios', datos);
}

function cambiarPassword(id, datos) {
  return api.patch('/usuarios/' + id + '/password', datos);
}

function activar(id) {
  return api.patch('/usuarios/' + id + '/activar');
}

function desactivar(id) {
  return api.patch('/usuarios/' + id + '/desactivar');
}

export default { listarTodos, guardar, cambiarPassword, activar, desactivar };