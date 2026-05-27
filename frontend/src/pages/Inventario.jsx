import { useState, useEffect } from 'react';
import Modal from '../components/Modal';
import inventarioApi from '../api/inventarioApi';
import referenciasApi from '../api/referenciasApi';
import '../styles/tabla.css';

// Tallas disponibles segun la base de datos
const TALLAS = [
  'T4','T6','T8','T10','T12','T14','T16','T18','T20','T22',
  'T28','T29','T30','T31','T32','T33','T34','T36','T38','T40','T42','T44',
  'XS','S','M','L','XL','XXL'
];

export default function Inventario() {

  // Estado para la lista de inventario
  const [inventario, setInventario] = useState([]);

  // Estado para la lista de referencias activas
  const [referencias, setReferencias] = useState([]);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mostrar u ocultar el modal
  const [mostrarModal, setMostrarModal] = useState(false);

  // Estado para saber si estamos editando o creando
  const [itemEditando, setItemEditando] = useState(null);

  // Estado para el buscador
  const [busqueda, setBusqueda] = useState('');

  // Estado para el filtro de marca
  const [marcaFiltro, setMarcaFiltro] = useState('');

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Estado del formulario
  const [form, setForm] = useState({
    referenciaId: '',
    talla: '',
    cantidad: '',
    precio: ''
  });

  // Carga los datos al montar el componente
  useEffect(function() {
    cargarDatos();
  }, []);

  // Carga el inventario y las referencias del backend
  async function cargarDatos() {
    setCargando(true);
    try {
      // Carga el inventario completo
      const respuestaInventario = await inventarioApi.listarTodo();
      setInventario(respuestaInventario.data);

      // Carga las referencias activas para el select del formulario
      const respuestaReferencias = await referenciasApi.listarActivas();
      setReferencias(respuestaReferencias.data);

    } catch (err) {
      console.error('Error al cargar inventario:', err);
    } finally {
      setCargando(false);
    }
  }

  // Abre el modal para crear un registro nuevo
  function handleNuevo() {
    // Limpia el formulario
    setForm({ referenciaId: '', talla: '', cantidad: '', precio: '' });
    // Limpia el item que se estaba editando
    setItemEditando(null);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Abre el modal para editar un registro existente
  function handleEditar(item) {
    // Carga los datos del item en el formulario
    setForm({
      referenciaId: item.referenciaId,
      talla: item.talla,
      cantidad: item.cantidad,
      precio: item.precio
    });
    // Guarda el item que se esta editando
    setItemEditando(item);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Cierra el modal
  function handleCerrarModal() {
    setMostrarModal(false);
    setItemEditando(null);
    setError('');
  }

  // Actualiza un campo del formulario
  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  // Guarda o actualiza el registro de inventario
  async function handleGuardar() {
    // Valida campos obligatorios
    if (form.referenciaId === '') {
      setError('La referencia es obligatoria');
      return;
    }

    if (form.talla === '') {
      setError('La talla es obligatoria');
      return;
    }

    if (form.cantidad === '' || Number(form.cantidad) < 0) {
      setError('La cantidad no puede ser negativa');
      return;
    }

    if (form.precio === '' || Number(form.precio) < 0) {
      setError('El precio no puede ser negativo');
      return;
    }

    setError('');

    // Arma el objeto request con los tipos correctos
    const request = {
      referenciaId: Number(form.referenciaId),
      talla: form.talla,
      cantidad: Number(form.cantidad),
      precio: Number(form.precio)
    };

    try {
      // Si hay item editando actualiza, si no crea uno nuevo
      if (itemEditando !== null) {
        await inventarioApi.actualizar(itemEditando.id, request);
      } else {
        await inventarioApi.guardar(request);
      }

      // Cierra el modal y recarga la lista
      handleCerrarModal();
      cargarDatos();

    } catch (err) {
      // Muestra el error del backend si existe
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al guardar el registro');
      }
    }
  }

  // Obtiene las marcas unicas para el filtro
  function obtenerMarcas() {
    // Crea un arreglo con las marcas sin repetir
    const marcas = [];
    for (let i = 0; i < inventario.length; i++) {
      if (marcas.includes(inventario[i].marca) === false) {
        marcas.push(inventario[i].marca);
      }
    }
    return marcas;
  }

  // Filtra el inventario segun busqueda y marca
  function inventarioFiltrado() {
    let resultado = inventario;

    // Filtra por marca si hay una seleccionada
    if (marcaFiltro !== '') {
      resultado = resultado.filter(function(item) {
        return item.marca === marcaFiltro;
      });
    }

    // Filtra por busqueda si hay texto
    if (busqueda.trim() !== '') {
      resultado = resultado.filter(function(item) {
        const marcaCoincide = item.marca.toLowerCase().includes(busqueda.toLowerCase());
        const refCoincide = item.referencia.toLowerCase().includes(busqueda.toLowerCase());
        return marcaCoincide || refCoincide;
      });
    }

    return resultado;
  }

  // Retorna la clase del badge segun la cantidad
  function obtenerClaseStock(cantidad) {
    if (cantidad === 0) {
      return 'badge badge-peligro';
    }
    if (cantidad <= 5) {
      return 'badge badge-pendiente';
    }
    return 'badge badge-exito';
  }

  // Formatea un numero como moneda colombiana
  function formatearMoneda(valor) {
    return '$' + Number(valor).toLocaleString('es-CO');
  }

  return (
    <div>
      {/* Titulo de la pagina */}
      <h1 className="titulo-pagina">Inventario</h1>

      {/* Tabla de inventario */}
      <div className="tabla-contenedor">

        {/* Toolbar con busqueda, filtro y boton nuevo */}
        <div className="tabla-toolbar">
          <input
            className="tabla-busqueda"
            placeholder="Buscar por marca o referencia..."
            value={busqueda}
            onChange={function(e) { setBusqueda(e.target.value); }}
          />

          {/* Filtro por marca */}
          <select
            style={{ width: 'auto' }}
            value={marcaFiltro}
            onChange={function(e) { setMarcaFiltro(e.target.value); }}>
            <option value="">Todas las marcas</option>
            {obtenerMarcas().map(function(marca) {
              return (
                <option key={marca} value={marca}>{marca}</option>
              );
            })}
          </select>

          <button className="btn-primario" onClick={handleNuevo}>
            + Agregar stock
          </button>
        </div>

        {/* Tabla */}
        <table className="tabla">
          <thead>
            <tr>
              <th>Marca</th>
              <th>Referencia</th>
              <th>Talla</th>
              <th>Cantidad</th>
              <th>Precio</th>
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

            {/* Sin registros */}
            {cargando === false && inventarioFiltrado().length === 0 && (
              <tr>
                <td colSpan={6} className="tabla-vacia">No hay registros en el inventario</td>
              </tr>
            )}

            {/* Filas de inventario */}
            {cargando === false && inventarioFiltrado().map(function(item) {
              return (
                <tr key={item.id}>
                  <td style={{ fontWeight: 500 }}>{item.marca}</td>
                  <td>{item.referencia}</td>
                  <td>
                    <span className="badge" style={{ background: 'var(--fondo-terciario)', color: 'var(--blanco-texto)' }}>
                      {item.talla}
                    </span>
                  </td>
                  <td>
                    <span className={obtenerClaseStock(item.cantidad)}>
                      {item.cantidad} uds
                    </span>
                  </td>
                  <td>{formatearMoneda(item.precio)}</td>
                  <td>
                    <div className="tabla-acciones">
                      {/* Boton editar */}
                      <button
                        className="tabla-btn-accion"
                        title="Editar cantidad y precio"
                        onClick={function() { handleEditar(item); }}>
                        ✏️
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
          <span>{inventarioFiltrado().length} registro(s) encontrado(s)</span>
        </div>
      </div>

      {/* Modal de crear o editar registro */}
      {mostrarModal && (
        <Modal
          titulo={itemEditando !== null ? 'Editar stock' : 'Agregar stock'}
          onCerrar={handleCerrarModal}>

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          {/* Campo referencia - solo al crear, no al editar */}
          {itemEditando === null && (
            <div className="modal-campo">
              <label className="modal-label">Referencia</label>
              <select
                value={form.referenciaId}
                onChange={function(e) { handleCampo('referenciaId', e.target.value); }}>
                <option value="">Selecciona una referencia</option>
                {referencias.map(function(ref) {
                  return (
                    <option key={ref.id} value={ref.id}>
                      {ref.marca} — {ref.referencia}
                    </option>
                  );
                })}
              </select>
            </div>
          )}

          {/* Si estamos editando mostramos la referencia como texto */}
          {itemEditando !== null && (
            <div className="modal-campo">
              <label className="modal-label">Referencia</label>
              <div style={{ padding: '10px 14px', background: 'var(--fondo-terciario)', borderRadius: 8, color: 'var(--gris-texto)', fontSize: 14 }}>
                {itemEditando.marca} — {itemEditando.referencia} — Talla {itemEditando.talla}
              </div>
            </div>
          )}

          {/* Campo talla - solo al crear */}
          {itemEditando === null && (
            <div className="modal-campo">
              <label className="modal-label">Talla</label>
              <select
                value={form.talla}
                onChange={function(e) { handleCampo('talla', e.target.value); }}>
                <option value="">Selecciona una talla</option>
                {TALLAS.map(function(talla) {
                  return (
                    <option key={talla} value={talla}>{talla}</option>
                  );
                })}
              </select>
            </div>
          )}

          {/* Campos cantidad y precio en dos columnas */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Cantidad</label>
              <input
                type="number"
                min="0"
                placeholder="0"
                value={form.cantidad}
                onChange={function(e) { handleCampo('cantidad', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">Precio ($)</label>
              <input
                type="number"
                min="0"
                placeholder="0"
                value={form.precio}
                onChange={function(e) { handleCampo('precio', e.target.value); }}
              />
            </div>
          </div>

          {/* Botones del modal */}
          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarModal}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleGuardar}>
              {itemEditando !== null ? 'Guardar cambios' : 'Agregar stock'}
            </button>
          </div>

        </Modal>
      )}
    </div>
  );
}