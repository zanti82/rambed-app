import { useState, useEffect } from 'react';
import Modal from '../components/Modal';
import clientesApi from '../api/clientesApi';
import vendedoresApi from '../api/vendedoresApi';
import { useAuth } from '../context/AuthContext';
import '../styles/tabla.css';

export default function Clientes() {

  // Estado para la lista de clientes
  const [clientes, setClientes] = useState([]);

  // Estado para la lista de vendedores activos
  const [vendedores, setVendedores] = useState([]);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mostrar u ocultar el modal
  const [mostrarModal, setMostrarModal] = useState(false);

  // Estado para saber si estamos editando o creando
  const [clienteEditando, setClienteEditando] = useState(null);

  // Estado para el buscador
  const [busqueda, setBusqueda] = useState('');

  // Estado para el filtro de ciudad
  const [ciudadFiltro, setCiudadFiltro] = useState('');

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Obtiene el usuario y funciones del contexto
  const { esAdmin, usuario } = useAuth();

  // Estado del formulario
  const [form, setForm] = useState({
    nombre: '',
    identificacion: '',
    ciudad: '',
    direccion: '',
    correo: '',
    telefono: '',
    nombreAlmacen: '',
    vendedorId: '',
    instagram: '',
    facebook: '',
    whatsapp: ''
  });

  // Carga los datos al montar el componente
  useEffect(function() {
    cargarDatos();
  }, []);

  // Carga los clientes y vendedores del backend
  async function cargarDatos() {
    setCargando(true);
    try {
      // Si es admin carga todos los clientes
      // Si es vendedor carga solo los suyos
      let respuestaClientes = null;
      if (esAdmin()) {
        respuestaClientes = await clientesApi.listarTodos();
      } else {
        respuestaClientes = await clientesApi.listarPorVendedor(usuario.vendedorId);
      }
      setClientes(respuestaClientes.data);

      // Carga los vendedores activos para el select del formulario
      const respuestaVendedores = await vendedoresApi.listarActivos();
      setVendedores(respuestaVendedores.data);

    } catch (err) {
      console.error('Error al cargar clientes:', err);
    } finally {
      setCargando(false);
    }
  }

  // Abre el modal para crear un cliente nuevo
  function handleNuevo() {
    // Limpia el formulario
    setForm({
      nombre: '',
      identificacion: '',
      ciudad: '',
      direccion: '',
      correo: '',
      telefono: '',
      nombreAlmacen: '',
      vendedorId: '',
      instagram: '',
      facebook: '',
      whatsapp: ''
    });
    // Limpia el cliente que se estaba editando
    setClienteEditando(null);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Abre el modal para editar un cliente existente
  function handleEditar(cliente) {
    // Carga los datos del cliente en el formulario
    setForm({
      nombre: cliente.nombre,
      identificacion: cliente.identificacion,
      ciudad: cliente.ciudad,
      direccion: cliente.direccion,
      correo: cliente.correo || '',
      telefono: cliente.telefono,
      nombreAlmacen: cliente.nombreAlmacen,
      vendedorId: cliente.vendedor.id,
      instagram: cliente.instagram || '',
      facebook: cliente.facebook || '',
      whatsapp: cliente.whatsapp || ''
    });
    // Guarda el cliente que se esta editando
    setClienteEditando(cliente);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Cierra el modal
  function handleCerrarModal() {
    setMostrarModal(false);
    setClienteEditando(null);
    setError('');
  }

  // Actualiza un campo del formulario
  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  // Guarda o actualiza el cliente
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

    if (form.ciudad.trim() === '') {
      setError('La ciudad es obligatoria');
      return;
    }

    if (form.direccion.trim() === '') {
      setError('La direccion es obligatoria');
      return;
    }

    if (form.telefono.trim() === '') {
      setError('El telefono es obligatorio');
      return;
    }

    if (form.nombreAlmacen.trim() === '') {
      setError('El nombre del almacen es obligatorio');
      return;
    }

    if (form.vendedorId === '') {
      setError('El vendedor es obligatorio');
      return;
    }

    setError('');

    // Arma el objeto request con los tipos correctos
    const request = {
      nombre: form.nombre,
      identificacion: form.identificacion,
      ciudad: form.ciudad,
      direccion: form.direccion,
      correo: form.correo,
      telefono: form.telefono,
      nombreAlmacen: form.nombreAlmacen,
      vendedor: { id: Number(form.vendedorId) },
      instagram: form.instagram,
      facebook: form.facebook,
      whatsapp: form.whatsapp
    };

    try {
      // Si hay cliente editando actualiza, si no crea uno nuevo
      if (clienteEditando !== null) {
        await clientesApi.actualizar(clienteEditando.id, request);
      } else {
        await clientesApi.guardar(request);
      }

      // Cierra el modal y recarga la lista
      handleCerrarModal();
      cargarDatos();

    } catch (err) {
      // Muestra el error del backend si existe
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al guardar el cliente');
      }
    }
  }

  // Activa o desactiva un cliente
  async function handleToggleActivo(cliente) {
    try {
      // Si esta activo lo desactiva, si no lo activa
      if (cliente.activo === 1) {
        await clientesApi.desactivar(cliente.id);
      } else {
        await clientesApi.activar(cliente.id);
      }
      // Recarga la lista
      cargarDatos();
    } catch (err) {
      console.error('Error al cambiar estado del cliente:', err);
    }
  }

  // Obtiene las ciudades unicas para el filtro
  function obtenerCiudades() {
    // Crea un arreglo con las ciudades sin repetir
    const ciudades = [];
    for (let i = 0; i < clientes.length; i++) {
      if (ciudades.includes(clientes[i].ciudad) === false) {
        ciudades.push(clientes[i].ciudad);
      }
    }
    return ciudades;
  }

  // Filtra los clientes segun busqueda y ciudad
  function clientesFiltrados() {
    let resultado = clientes;

    // Filtra por ciudad si hay una seleccionada
    if (ciudadFiltro !== '') {
      resultado = resultado.filter(function(cliente) {
        return cliente.ciudad === ciudadFiltro;
      });
    }

    // Filtra por busqueda si hay texto
    if (busqueda.trim() !== '') {
      resultado = resultado.filter(function(cliente) {
        const nombreCoincide = cliente.nombre.toLowerCase().includes(busqueda.toLowerCase());
        const almacenCoincide = cliente.nombreAlmacen.toLowerCase().includes(busqueda.toLowerCase());
        const idCoincide = cliente.identificacion.toLowerCase().includes(busqueda.toLowerCase());
        return nombreCoincide || almacenCoincide || idCoincide;
      });
    }

    return resultado;
  }

  return (
    <div>
      {/* Titulo de la pagina */}
      <h1 className="titulo-pagina">Clientes</h1>

      {/* Tabla de clientes */}
      <div className="tabla-contenedor">

        {/* Toolbar con busqueda, filtro y boton nuevo */}
        <div className="tabla-toolbar">
          <input
            className="tabla-busqueda"
            placeholder="Buscar por nombre, almacen o identificacion..."
            value={busqueda}
            onChange={function(e) { setBusqueda(e.target.value); }}
          />

          {/* Filtro por ciudad */}
          <select
            style={{ width: 'auto' }}
            value={ciudadFiltro}
            onChange={function(e) { setCiudadFiltro(e.target.value); }}>
            <option value="">Todas las ciudades</option>
            {obtenerCiudades().map(function(ciudad) {
              return (
                <option key={ciudad} value={ciudad}>{ciudad}</option>
              );
            })}
          </select>

          <button className="btn-primario" onClick={handleNuevo}>
            + Nuevo cliente
          </button>
        </div>

        {/* Tabla */}
        <table className="tabla">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Almacén</th>
              <th>Ciudad</th>
              <th>Teléfono</th>
              <th>Vendedor</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>

            {/* Estado de carga */}
            {cargando && (
              <tr>
                <td colSpan={7} className="tabla-vacia">Cargando...</td>
              </tr>
            )}

            {/* Sin clientes */}
            {cargando === false && clientesFiltrados().length === 0 && (
              <tr>
                <td colSpan={7} className="tabla-vacia">No hay clientes registrados</td>
              </tr>
            )}

            {/* Filas de clientes */}
            {cargando === false && clientesFiltrados().map(function(cliente) {
              return (
                <tr key={cliente.id}>
                  <td>
                    <div style={{ fontWeight: 500 }}>{cliente.nombre}</div>
                    <div style={{ fontSize: 12, color: 'var(--gris-texto)' }}>{cliente.identificacion}</div>
                  </td>
                  <td>{cliente.nombreAlmacen}</td>
                  <td>{cliente.ciudad}</td>
                  <td>{cliente.telefono}</td>
                  <td>{cliente.vendedor ? cliente.vendedor.nombre : '—'}</td>
                  <td>
                    {cliente.activo === 1
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
                        onClick={function() { handleEditar(cliente); }}>
                        ✏️
                      </button>
                      {/* Boton activar o desactivar */}
                      <button
                        className="tabla-btn-accion"
                        title={cliente.activo === 1 ? 'Desactivar' : 'Activar'}
                        onClick={function() { handleToggleActivo(cliente); }}>
                        {cliente.activo === 1 ? '🔴' : '🟢'}
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
          <span>{clientesFiltrados().length} cliente(s) encontrado(s)</span>
        </div>
      </div>

      {/* Modal de crear o editar cliente */}
      {mostrarModal && (
        <Modal
          titulo={clienteEditando !== null ? 'Editar cliente' : 'Nuevo cliente'}
          onCerrar={handleCerrarModal}>

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          {/* Campos nombre e identificacion */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Nombre completo</label>
              <input
                type="text"
                placeholder="Ej: Juan Lopez"
                value={form.nombre}
                onChange={function(e) { handleCampo('nombre', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">Identificación</label>
              <input
                type="text"
                placeholder="Ej: 1234567890"
                value={form.identificacion}
                onChange={function(e) { handleCampo('identificacion', e.target.value); }}
              />
            </div>
          </div>

          {/* Campo nombre almacen */}
          <div className="modal-campo">
            <label className="modal-label">Nombre del almacén</label>
            <input
              type="text"
              placeholder="Ej: Almacen Centro"
              value={form.nombreAlmacen}
              onChange={function(e) { handleCampo('nombreAlmacen', e.target.value); }}
            />
          </div>

          {/* Campos ciudad y telefono */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Ciudad</label>
              <input
                type="text"
                placeholder="Ej: Bogota"
                value={form.ciudad}
                onChange={function(e) { handleCampo('ciudad', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">Teléfono</label>
              <input
                type="text"
                placeholder="Ej: 3001234567"
                value={form.telefono}
                onChange={function(e) { handleCampo('telefono', e.target.value); }}
              />
            </div>
          </div>

          {/* Campo direccion */}
          <div className="modal-campo">
            <label className="modal-label">Dirección</label>
            <input
              type="text"
              placeholder="Ej: Calle 10 # 20-30"
              value={form.direccion}
              onChange={function(e) { handleCampo('direccion', e.target.value); }}
            />
          </div>

          {/* Campos correo y vendedor */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Correo (opcional)</label>
              <input
                type="email"
                placeholder="Ej: correo@gmail.com"
                value={form.correo}
                onChange={function(e) { handleCampo('correo', e.target.value); }}
              />
            </div>
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
          </div>

          {/* Seccion de redes sociales */}
          <div style={{ marginBottom: 8, marginTop: 8, fontSize: 12, color: 'var(--gris-texto)', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: 500 }}>
            Redes sociales (opcionales)
          </div>

          {/* Campos de redes sociales */}
          <div className="modal-grid-3">
            <div className="modal-campo">
              <label className="modal-label">Instagram</label>
              <input
                type="text"
                placeholder="@usuario"
                value={form.instagram}
                onChange={function(e) { handleCampo('instagram', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">Facebook</label>
              <input
                type="text"
                placeholder="@usuario"
                value={form.facebook}
                onChange={function(e) { handleCampo('facebook', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">WhatsApp</label>
              <input
                type="text"
                placeholder="3001234567"
                value={form.whatsapp}
                onChange={function(e) { handleCampo('whatsapp', e.target.value); }}
              />
            </div>
          </div>

          {/* Botones del modal */}
          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarModal}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleGuardar}>
              {clienteEditando !== null ? 'Guardar cambios' : 'Crear cliente'}
            </button>
          </div>

        </Modal>
      )}
    </div>
  );
}