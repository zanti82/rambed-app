import { useState, useEffect } from 'react';
import Modal from '../components/Modal';
import devolucionesApi from '../api/devolucionesApi';
import facturasApi from '../api/facturasApi';
import facturaDetalleApi from '../api/facturaDetalleApi';
import { useAuth } from '../context/AuthContext';
import '../styles/tabla.css';

export default function Devoluciones() {

  // Estado para la lista de devoluciones
  const [devoluciones, setDevoluciones] = useState([]);

  // Estado para las facturas pagadas disponibles para devolucion
  const [facturas, setFacturas] = useState([]);

  // Estado para los items de la factura seleccionada
  const [itemsFactura, setItemsFactura] = useState([]);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mostrar u ocultar el modal
  const [mostrarModal, setMostrarModal] = useState(false);

  // Estado para el buscador
  const [busqueda, setBusqueda] = useState('');

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Estado de carga de items de factura
  const [cargandoItems, setCargandoItems] = useState(false);

  // Estado del formulario de devolucion
  const [form, setForm] = useState({
    facturaId: '',
    facturaDetalleId: '',
    cantidad: '',
    motivo: ''
  });

  // Obtiene el usuario y funciones del contexto
  const { esAdmin, usuario } = useAuth();

  // Carga los datos al montar el componente
  useEffect(function() {
    cargarDatos();
  }, []);

  // Cuando se selecciona una factura carga sus items
  useEffect(function() {
    // Si hay una factura seleccionada carga sus items
    if (form.facturaId !== '') {
      cargarItemsFactura(form.facturaId);
    } else {
      // Si no hay factura limpia los items
      setItemsFactura([]);
    }
  }, [form.facturaId]);

  // Carga las devoluciones y facturas pagadas del backend
  async function cargarDatos() {
    setCargando(true);
    try {
      // Carga todas las devoluciones
      const respuestaDevoluciones = await devolucionesApi.listarTodas();
      setDevoluciones(respuestaDevoluciones.data);

      // Carga las facturas pagadas para el formulario
      const respuestaFacturas = await facturasApi.listarPorEstado('pagada');
      setFacturas(respuestaFacturas.data);

    } catch (err) {
      console.error('Error al cargar devoluciones:', err);
    } finally {
      setCargando(false);
    }
  }

  // Carga los items de una factura especifica
  async function cargarItemsFactura(facturaId) {
    setCargandoItems(true);
    try {
      const respuesta = await facturaDetalleApi.listarPorFactura(facturaId);
      setItemsFactura(respuesta.data);
    } catch (err) {
      console.error('Error al cargar items de factura:', err);
    } finally {
      setCargandoItems(false);
    }
  }

  // Abre el modal para registrar una devolucion
  function handleNueva() {
    // Limpia el formulario
    setForm({ facturaId: '', facturaDetalleId: '', cantidad: '', motivo: '' });
    // Limpia los items
    setItemsFactura([]);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Cierra el modal
  function handleCerrarModal() {
    setMostrarModal(false);
    setItemsFactura([]);
    setError('');
  }

  // Actualiza un campo del formulario
  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  // Registra la devolucion
  async function handleGuardar() {
    // Valida campos obligatorios
    if (form.facturaId === '') {
      setError('La factura es obligatoria');
      return;
    }

    if (form.facturaDetalleId === '') {
      setError('El producto a devolver es obligatorio');
      return;
    }

    if (form.cantidad === '' || Number(form.cantidad) <= 0) {
      setError('La cantidad debe ser mayor a cero');
      return;
    }

    setError('');

    // Arma el request de la devolucion
    const request = {
      facturaDetalleId: Number(form.facturaDetalleId),
      cantidad: Number(form.cantidad),
      motivo: form.motivo
    };

    try {
      // Registra la devolucion en el backend
      await devolucionesApi.registrar(request);

      // Cierra el modal y recarga la lista
      handleCerrarModal();
      cargarDatos();

    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al registrar la devolucion');
      }
    }
  }

  // Filtra las devoluciones segun la busqueda
  function devolucionesFiltradas() {
    // Si no hay busqueda retorna todas
    if (busqueda.trim() === '') {
      return devoluciones;
    }

    // Filtra por numero de factura, marca o referencia
    return devoluciones.filter(function(dev) {
      const facturaCoincide = dev.numeroFactura.toLowerCase().includes(busqueda.toLowerCase());
      const marcaCoincide = dev.marca.toLowerCase().includes(busqueda.toLowerCase());
      const refCoincide = dev.referencia.toLowerCase().includes(busqueda.toLowerCase());
      return facturaCoincide || marcaCoincide || refCoincide;
    });
  }

  // Obtiene el item seleccionado del detalle para mostrar el stock maximo
  function obtenerItemSeleccionado() {
    // Busca el item en la lista de items de la factura
    const item = itemsFactura.find(function(i) {
      return i.id === Number(form.facturaDetalleId);
    });
    return item || null;
  }

  // Formatea una fecha para mostrarla
  function formatearFecha(fecha) {
    if (fecha === null || fecha === undefined) {
      return '—';
    }
    return new Date(fecha).toLocaleDateString('es-CO');
  }

  return (
    <div>
      {/* Titulo de la pagina */}
      <h1 className="titulo-pagina">Devoluciones</h1>

      {/* Tabla de devoluciones */}
      <div className="tabla-contenedor">

        {/* Toolbar con busqueda y boton nuevo */}
        <div className="tabla-toolbar">
          <input
            className="tabla-busqueda"
            placeholder="Buscar por factura, marca o referencia..."
            value={busqueda}
            onChange={function(e) { setBusqueda(e.target.value); }}
          />
          <button className="btn-primario" onClick={handleNueva}>
            + Registrar devolucion
          </button>
        </div>

        {/* Tabla */}
        <table className="tabla">
          <thead>
            <tr>
              <th>Factura</th>
              <th>Producto</th>
              <th>Talla</th>
              <th>Cantidad</th>
              <th>Motivo</th>
              <th>Fecha</th>
            </tr>
          </thead>
          <tbody>

            {/* Estado de carga */}
            {cargando && (
              <tr>
                <td colSpan={6} className="tabla-vacia">Cargando...</td>
              </tr>
            )}

            {/* Sin devoluciones */}
            {cargando === false && devolucionesFiltradas().length === 0 && (
              <tr>
                <td colSpan={6} className="tabla-vacia">No hay devoluciones registradas</td>
              </tr>
            )}

            {/* Filas de devoluciones */}
            {cargando === false && devolucionesFiltradas().map(function(dev) {
              return (
                <tr key={dev.id}>
                  <td style={{ fontWeight: 500 }}>{dev.numeroFactura}</td>
                  <td>
                    <div style={{ fontWeight: 500 }}>{dev.marca}</div>
                    <div style={{ fontSize: 12, color: 'var(--gris-texto)' }}>{dev.referencia}</div>
                  </td>
                  <td>
                    <span className="badge" style={{ background: 'var(--fondo-terciario)', color: 'var(--blanco-texto)' }}>
                      {dev.talla}
                    </span>
                  </td>
                  <td>
                    <span className="badge badge-peligro">{dev.cantidad} uds</span>
                  </td>
                  <td style={{ color: 'var(--gris-texto)' }}>{dev.motivo || '—'}</td>
                  <td style={{ color: 'var(--gris-texto)' }}>{formatearFecha(dev.creadoEn)}</td>
                </tr>
              );
            })}
          </tbody>
        </table>

        {/* Contador de resultados */}
        <div className="tabla-paginacion">
          <span>{devolucionesFiltradas().length} devolucion(es) encontrada(s)</span>
        </div>
      </div>

      {/* Modal de registrar devolucion */}
      {mostrarModal && (
        <Modal
          titulo="Registrar devolución"
          onCerrar={handleCerrarModal}>

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          {/* Campo factura */}
          <div className="modal-campo">
            <label className="modal-label">Factura pagada</label>
            <select
              value={form.facturaId}
              onChange={function(e) {
                // Actualiza el facturaId y limpia el item seleccionado
                setForm({ ...form, facturaId: e.target.value, facturaDetalleId: '' });
              }}>
              <option value="">Selecciona una factura</option>
              {facturas.map(function(factura) {
                return (
                  <option key={factura.id} value={factura.id}>
                    {factura.numeroFactura} — {factura.clienteNombre}
                  </option>
                );
              })}
            </select>
          </div>

          {/* Campo producto - solo si hay factura seleccionada */}
          {form.facturaId !== '' && (
            <div className="modal-campo">
              <label className="modal-label">Producto a devolver</label>

              {/* Estado de carga de items */}
              {cargandoItems && (
                <div style={{ padding: '10px 14px', background: 'var(--fondo-terciario)', borderRadius: 8, color: 'var(--gris-texto)', fontSize: 14 }}>
                  Cargando productos...
                </div>
              )}

              {/* Select de items */}
              {cargandoItems === false && (
                <select
                  value={form.facturaDetalleId}
                  onChange={function(e) { handleCampo('facturaDetalleId', e.target.value); }}>
                  <option value="">Selecciona un producto</option>
                  {itemsFactura.map(function(item) {
                    return (
                      <option key={item.id} value={item.id}>
                        {item.marca} {item.referencia} — T{item.talla} ({item.cantidad} uds vendidas)
                      </option>
                    );
                  })}
                </select>
              )}
            </div>
          )}

          {/* Campo cantidad */}
          {form.facturaDetalleId !== '' && (
            <div className="modal-campo">
              <label className="modal-label">
                Cantidad a devolver
                {/* Muestra el maximo disponible */}
                {obtenerItemSeleccionado() !== null && (
                  <span style={{ color: 'var(--gris-texto)', fontWeight: 400, marginLeft: 8 }}>
                    (máx. {obtenerItemSeleccionado().cantidad})
                  </span>
                )}
              </label>
              <input
                type="number"
                min="1"
                max={obtenerItemSeleccionado() !== null ? obtenerItemSeleccionado().cantidad : undefined}
                placeholder="0"
                value={form.cantidad}
                onChange={function(e) { handleCampo('cantidad', e.target.value); }}
              />
            </div>
          )}

          {/* Campo motivo */}
          <div className="modal-campo">
            <label className="modal-label">Motivo (opcional)</label>
            <input
              type="text"
              placeholder="Ej: Producto defectuoso, talla incorrecta..."
              value={form.motivo}
              onChange={function(e) { handleCampo('motivo', e.target.value); }}
            />
          </div>

          {/* Botones del modal */}
          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarModal}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleGuardar}>
              Registrar devolución
            </button>
          </div>

        </Modal>
      )}
    </div>
  );
}