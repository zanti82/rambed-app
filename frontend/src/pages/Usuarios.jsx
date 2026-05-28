import { useState, useEffect } from 'react';
import Modal from '../components/Modal';
import usuariosApi from '../api/usuariosApi';
import vendedoresApi from '../api/vendedoresApi';
import '../styles/tabla.css';

export default function Usuarios() {

  const [usuarios, setUsuarios] = useState([]);
  const [vendedores, setVendedores] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [mostrarModal, setMostrarModal] = useState(false);
  const [mostrarModalPassword, setMostrarModalPassword] = useState(false);
  const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
  const [error, setError] = useState('');
  const [errorPassword, setErrorPassword] = useState('');

  // Estado del formulario de crear usuario
  const [form, setForm] = useState({
    username: '',
    password: '',
    rolId: '',
    vendedorId: ''
  });

  // Estado del formulario de cambiar password
  const [formPassword, setFormPassword] = useState({
    nuevaPassword: ''
  });

  useEffect(function() {
    cargarDatos();
  }, []);

  async function cargarDatos() {
    setCargando(true);
    try {
      // Carga usuarios y vendedores al mismo tiempo
      const respuestaUsuarios = await usuariosApi.listarTodos();
      setUsuarios(respuestaUsuarios.data);

      const respuestaVendedores = await vendedoresApi.listarActivos();
      setVendedores(respuestaVendedores.data);
    } catch (err) {
      console.error('Error al cargar datos:', err);
    } finally {
      setCargando(false);
    }
  }

  function handleNuevo() {
    setForm({ username: '', password: '', rolId: '', vendedorId: '' });
    setError('');
    setMostrarModal(true);
  }

  function handleCerrarModal() {
    setMostrarModal(false);
    setError('');
  }

  function handleAbrirPassword(usuario) {
    setUsuarioSeleccionado(usuario);
    setFormPassword({ nuevaPassword: '' });
    setErrorPassword('');
    setMostrarModalPassword(true);
  }

  function handleCerrarPassword() {
    setMostrarModalPassword(false);
    setUsuarioSeleccionado(null);
    setErrorPassword('');
  }

  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  async function handleGuardar() {
    // Valida campos obligatorios
    if (form.username.trim() === '') {
      setError('El username es obligatorio');
      return;
    }

    if (form.password.trim() === '') {
      setError('El password es obligatorio');
      return;
    }

    if (form.password.length < 6) {
      setError('El password debe tener minimo 6 caracteres');
      return;
    }

    if (form.rolId === '') {
      setError('El rol es obligatorio');
      return;
    }

    // Si el rol es vendedor valida que venga el vendedor
    if (Number(form.rolId) === 2 && form.vendedorId === '') {
      setError('El vendedor es obligatorio para el rol VENDEDOR');
      return;
    }

    setError('');

    // Arma el request
    const request = {
      username: form.username.trim(),
      password: form.password,
      rolId: Number(form.rolId),
      vendedorId: form.vendedorId !== '' ? Number(form.vendedorId) : null
    };

    try {
      await usuariosApi.guardar(request);
      handleCerrarModal();
      cargarDatos();
    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al crear el usuario');
      }
    }
  }

  async function handleCambiarPassword() {
    // Valida que el password no venga vacio
    if (formPassword.nuevaPassword.trim() === '') {
      setErrorPassword('El nuevo password es obligatorio');
      return;
    }

    if (formPassword.nuevaPassword.length < 6) {
      setErrorPassword('El password debe tener minimo 6 caracteres');
      return;
    }

    setErrorPassword('');

    try {
      await usuariosApi.cambiarPassword(usuarioSeleccionado.id, formPassword);
      handleCerrarPassword();
    } catch (err) {
      if (err.response && err.response.data) {
        setErrorPassword(err.response.data);
      } else {
        setErrorPassword('Error al cambiar el password');
      }
    }
  }

  async function handleToggleActivo(usuario) {
    try {
      if (usuario.activo === 1) {
        await usuariosApi.desactivar(usuario.id);
      } else {
        await usuariosApi.activar(usuario.id);
      }
      cargarDatos();
    } catch (err) {
      console.error('Error al cambiar estado:', err);
    }
  }

  return (
    <div>
      <h1 className="titulo-pagina">Usuarios</h1>

      <div className="tabla-contenedor">
        <div className="tabla-toolbar">
          <span style={{ fontSize: 13, color: 'var(--gris-texto)' }}>
            {usuarios.length} usuario(s) registrado(s)
          </span>
          <button className="btn-primario" onClick={handleNuevo}>
            + Nuevo usuario
          </button>
        </div>

        <table className="tabla">
          <thead>
            <tr>
              <th>Username</th>
              <th>Rol</th>
              <th>Vendedor asignado</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>

            {cargando && (
              <tr>
                <td colSpan={5} className="tabla-vacia">Cargando...</td>
              </tr>
            )}

            {cargando === false && usuarios.length === 0 && (
              <tr>
                <td colSpan={5} className="tabla-vacia">No hay usuarios registrados</td>
              </tr>
            )}

            {cargando === false && usuarios.map(function(usuario) {
              return (
                <tr key={usuario.id}>
                  <td style={{ fontWeight: 500 }}>{usuario.username}</td>
                  <td>
                    <span className={usuario.rol === 'ADMIN' ? 'badge badge-peligro' : 'badge badge-exito'}>
                      {usuario.rol}
                    </span>
                  </td>
                  <td>{usuario.vendedorNombre || '—'}</td>
                  <td>
                    {usuario.activo === 1
                      ? <span className="badge badge-exito">Activo</span>
                      : <span className="badge badge-peligro">Inactivo</span>
                    }
                  </td>
                  <td>
                    <div className="tabla-acciones">
                      {/* Boton cambiar password */}
                      <button
                        className="tabla-btn-accion"
                        title="Cambiar password"
                        onClick={function() { handleAbrirPassword(usuario); }}>
                        🔑
                      </button>
                      {/* Boton activar o desactivar */}
                      <button
                        className="tabla-btn-accion"
                        title={usuario.activo === 1 ? 'Desactivar' : 'Activar'}
                        onClick={function() { handleToggleActivo(usuario); }}>
                        {usuario.activo === 1 ? '🔴' : '🟢'}
                      </button>
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>

        <div className="tabla-paginacion">
          <span>{usuarios.length} usuario(s)</span>
        </div>
      </div>

      {/* Modal crear usuario */}
      {mostrarModal && (
        <Modal titulo="Nuevo usuario" onCerrar={handleCerrarModal}>

          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          <div className="modal-campo">
            <label className="modal-label">Username</label>
            <input
              type="text"
              placeholder="Ej: carlos.mendez"
              value={form.username}
              onChange={function(e) { handleCampo('username', e.target.value); }}
            />
          </div>

          <div className="modal-campo">
            <label className="modal-label">Password</label>
            <input
              type="password"
              placeholder="Minimo 6 caracteres"
              value={form.password}
              onChange={function(e) { handleCampo('password', e.target.value); }}
            />
          </div>

          <div className="modal-campo">
            <label className="modal-label">Rol</label>
            <select
              value={form.rolId}
              onChange={function(e) { handleCampo('rolId', e.target.value); }}>
              <option value="">Selecciona un rol</option>
              <option value="1">ADMIN</option>
              <option value="2">VENDEDOR</option>
            </select>
          </div>

          {/* Campo vendedor solo si el rol es VENDEDOR */}
          {Number(form.rolId) === 2 && (
            <div className="modal-campo">
              <label className="modal-label">Vendedor asignado</label>
              <select
                value={form.vendedorId}
                onChange={function(e) { handleCampo('vendedorId', e.target.value); }}>
                <option value="">Selecciona un vendedor</option>
                {vendedores.map(function(vendedor) {
                  return (
                    <option key={vendedor.id} value={vendedor.id}>
                      {vendedor.nombre}
                    </option>
                  );
                })}
              </select>
            </div>
          )}

          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarModal}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleGuardar}>
              Crear usuario
            </button>
          </div>

        </Modal>
      )}

      {/* Modal cambiar password */}
      {mostrarModalPassword && usuarioSeleccionado !== null && (
        <Modal
          titulo={'Cambiar password — ' + usuarioSeleccionado.username}
          onCerrar={handleCerrarPassword}>

          {errorPassword !== '' && (
            <div className="error-mensaje">{errorPassword}</div>
          )}

          <div className="modal-campo">
            <label className="modal-label">Nueva contraseña</label>
            <input
              type="password"
              placeholder="Minimo 6 caracteres"
              value={formPassword.nuevaPassword}
              onChange={function(e) { setFormPassword({ nuevaPassword: e.target.value }); }}
            />
          </div>

          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarPassword}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleCambiarPassword}>
              Cambiar password
            </button>
          </div>

        </Modal>
      )}
    </div>
  );
}