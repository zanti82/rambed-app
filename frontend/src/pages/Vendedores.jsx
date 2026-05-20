import { useState, useEffect } from 'react';
import Modal from '../components/Modal';
import vendedoresApi from '../api/vendedoresApi';
import '../styles/tabla.css';

export default function Vendedores() {

  // Estado para la lista de vendedores
  const [vendedores, setVendedores] = useState([]);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mostrar u ocultar el modal
  const [mostrarModal, setMostrarModal] = useState(false);

  // Estado para saber si estamos editando o creando
  const [vendedorEditando, setVendedorEditando] = useState(null);

  // Estado para el buscador
  const [busqueda, setBusqueda] = useState('');

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Estado del formulario
  const [form, setForm] = useState({
    nombre: '',
    identificacion: '',
    telefono: '',
    correo: ''
  });

  // Carga los vendedores al montar el componente
  useEffect(function() {
    cargarVendedores();
  }, []);

  // Obtiene todos los vendedores del backend
  async function cargarVendedores() {
    setCargando(true);
    try {
      const respuesta = await vendedoresApi.listarTodos();
      setVendedores(respuesta.data);
    } catch (err) {
      console.error('Error al cargar vendedores:', err);
    } finally {
      setCargando(false);
    }
  }

  // Abre el modal para crear un vendedor nuevo
  function handleNuevo() {
    // Limpia el formulario
    setForm({ nombre: '', identificacion: '', telefono: '', correo: '' });
    // Limpia el vendedor que se estaba editando
    setVendedorEditando(null);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Abre el modal para editar un vendedor existente
  function handleEditar(vendedor) {
    // Carga los datos del vendedor en el formulario
    setForm({
      nombre: vendedor.nombre,
      identificacion: vendedor.identificacion,
      telefono: vendedor.telefono || '',
      correo: vendedor.correo || ''
    });
    // Guarda el vendedor que se esta editando
    setVendedorEditando(vendedor);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Cierra el modal
  function handleCerrarModal() {
    setMostrarModal(false);
    setVendedorEditando(null);
    setError('');
  }

  // Actualiza un campo del formulario
  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  // Guarda o actualiza el vendedor
  async function handleGuardar() {
    // Valida campos obligatorios
    if (form.nombre.trim() === '') {
      setError('El nombre es obligatorio');
      return;
    }

    if (form.identificacion.trim() === '') {
      setError('La identificacion es obligatoria');
      return;
    }

    setError('');

    try {
      // Si hay vendedor editando actualiza, si no crea uno nuevo
      if (vendedorEditando !== null) {
        await vendedoresApi.actualizar(vendedorEditando.id, form);
      } else {
        await vendedoresApi.guardar(form);
      }

      // Cierra el modal y recarga la lista
      handleCerrarModal();
      cargarVendedores();

    } catch (err) {
      // Muestra el error del backend si existe
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al guardar el vendedor');
      }
    }
  }

  // Activa o desactiva un vendedor
  async function handleToggleActivo(vendedor) {
    try {
      // Si esta activo lo desactiva, si no lo activa
      if (vendedor.activo === 1) {
        await vendedoresApi.desactivar(vendedor.id);
      } else {
        await vendedoresApi.activar(vendedor.id);
      }
      // Recarga la lista
      cargarVendedores();
    } catch (err) {
      console.error('Error al cambiar estado del vendedor:', err);
    }
  }

  // Filtra los vendedores segun la busqueda
  function vendedoresFiltrados() {
    // Si no hay busqueda retorna todos
    if (busqueda.trim() === '') {
      return vendedores;
    }

    // Filtra por nombre o identificacion
    return vendedores.filter(function(vendedor) {
      const nombreCoincide = vendedor.nombre.toLowerCase().includes(busqueda.toLowerCase());
      const idCoincide = vendedor.identificacion.toLowerCase().includes(busqueda.toLowerCase());
      return nombreCoincide || idCoincide;
    });
  }

  return (
    <div>
      {/* Titulo de la pagina */}
      <h1 className="titulo-pagina">Vendedores</h1>

      {/* Tabla de vendedores */}
      <div className="tabla-contenedor">

        {/* Toolbar con busqueda y boton nuevo */}
        <div className="tabla-toolbar">
          <input
            className="tabla-busqueda"
            placeholder="Buscar por nombre o identificacion..."
            value={busqueda}
            onChange={function(e) { setBusqueda(e.target.value); }}
          />
          <button className="btn-primario" onClick={handleNuevo}>
            + Nuevo vendedor
          </button>
        </div>

        {/* Tabla */}
        <table className="tabla">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Identificación</th>
              <th>Teléfono</th>
              <th>Correo</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>

            {/* Estado de carga */}
            {cargando && (
              <tr>
                <td colSpan={6} className="tabla-vacia">Cargando...</td>
              </tr>
            )}

            {/* Sin vendedores */}
            {cargando === false && vendedoresFiltrados().length === 0 && (
              <tr>
                <td colSpan={6} className="tabla-vacia">No hay vendedores registrados</td>
              </tr>
            )}

            {/* Filas de vendedores */}
            {cargando === false && vendedoresFiltrados().map(function(vendedor) {
              return (
                <tr key={vendedor.id}>
                  <td style={{ fontWeight: 500 }}>{vendedor.nombre}</td>
                  <td>{vendedor.identificacion}</td>
                  <td>{vendedor.telefono || '—'}</td>
                  <td>{vendedor.correo || '—'}</td>
                  <td>
                    {vendedor.activo === 1
                      ? <span className="badge badge-exito">Activo</span>
                      : <span className="badge badge-peligro">Inactivo</span>
                    }
                  </td>
                  <td>
                    <div className="tabla-acciones">
                      {/* Boton editar */}
                      <button
                        className="tabla-btn-accion"
                        title="Editar"
                        onClick={function() { handleEditar(vendedor); }}>
                        ✏️
                      </button>
                      {/* Boton activar o desactivar */}
                      <button
                        className="tabla-btn-accion"
                        title={vendedor.activo === 1 ? 'Desactivar' : 'Activar'}
                        onClick={function() { handleToggleActivo(vendedor); }}>
                        {vendedor.activo === 1 ? '🔴' : '🟢'}
                      </button>
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>

        {/* Contador de resultados */}
        <div className="tabla-paginacion">
          <span>{vendedoresFiltrados().length} vendedor(es) encontrado(s)</span>
        </div>
      </div>

      {/* Modal de crear o editar vendedor */}
      {mostrarModal && (
        <Modal
          titulo={vendedorEditando !== null ? 'Editar vendedor' : 'Nuevo vendedor'}
          onCerrar={handleCerrarModal}>

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          {/* Campo nombre */}
          <div className="modal-campo">
            <label className="modal-label">Nombre completo</label>
            <input
              type="text"
              placeholder="Ej: Carlos Mendez"
              value={form.nombre}
              onChange={function(e) { handleCampo('nombre', e.target.value); }}
            />
          </div>

          {/* Campo identificacion */}
          <div className="modal-campo">
            <label className="modal-label">Identificación</label>
            <input
              type="text"
              placeholder="Ej: 1234567890"
              value={form.identificacion}
              onChange={function(e) { handleCampo('identificacion', e.target.value); }}
            />
          </div>

          {/* Campos telefono y correo en dos columnas */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Teléfono</label>
              <input
                type="text"
                placeholder="Ej: 3001234567"
                value={form.telefono}
                onChange={function(e) { handleCampo('telefono', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">Correo</label>
              <input
                type="email"
                placeholder="Ej: correo@gmail.com"
                value={form.correo}
                onChange={function(e) { handleCampo('correo', e.target.value); }}
              />
            </div>
          </div>

          {/* Botones del modal */}
          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarModal}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleGuardar}>
              {vendedorEditando !== null ? 'Guardar cambios' : 'Crear vendedor'}
            </button>
          </div>

        </Modal>
      )}
    </div>
  );
}