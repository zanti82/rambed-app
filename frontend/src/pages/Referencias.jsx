import { useState, useEffect } from 'react';
import Modal from '../components/Modal';
import referenciasApi from '../api/referenciasApi';
import '../styles/tabla.css';

export default function Referencias() {

  // Estado para la lista de referencias
  const [referencias, setReferencias] = useState([]);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mostrar u ocultar el modal
  const [mostrarModal, setMostrarModal] = useState(false);

  // Estado para saber si estamos editando o creando
  const [referenciaEditando, setReferenciaEditando] = useState(null);

  // Estado para el buscador
  const [busqueda, setBusqueda] = useState('');

  // Estado para el filtro de marca
  const [marcaFiltro, setMarcaFiltro] = useState('');

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Estado del formulario
  const [form, setForm] = useState({
    marca: '',
    referencia: '',
    descripcion: ''
  });

  // Carga las referencias al montar el componente
  useEffect(function() {
    cargarReferencias();
  }, []);

  // Obtiene todas las referencias del backend
  async function cargarReferencias() {
    setCargando(true);
    try {
      const respuesta = await referenciasApi.listarTodas();
      setReferencias(respuesta.data);
    } catch (err) {
      console.error('Error al cargar referencias:', err);
    } finally {
      setCargando(false);
    }
  }

  // Abre el modal para crear una referencia nueva
  function handleNuevo() {
    // Limpia el formulario
    setForm({ marca: '', referencia: '', descripcion: '' });
    // Limpia la referencia que se estaba editando
    setReferenciaEditando(null);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Abre el modal para editar una referencia existente
  function handleEditar(referencia) {
    // Carga los datos de la referencia en el formulario
    setForm({
      marca: referencia.marca,
      referencia: referencia.referencia,
      descripcion: referencia.descripcion || ''
    });
    // Guarda la referencia que se esta editando
    setReferenciaEditando(referencia);
    // Limpia el error
    setError('');
    // Abre el modal
    setMostrarModal(true);
  }

  // Cierra el modal
  function handleCerrarModal() {
    setMostrarModal(false);
    setReferenciaEditando(null);
    setError('');
  }

  // Actualiza un campo del formulario
  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  // Guarda o actualiza la referencia
  async function handleGuardar() {
    // Valida campos obligatorios
    if (form.marca.trim() === '') {
      setError('La marca es obligatoria');
      return;
    }

    if (form.referencia.trim() === '') {
      setError('La referencia es obligatoria');
      return;
    }

    setError('');

    try {
      // Si hay referencia editando actualiza, si no crea una nueva
      if (referenciaEditando !== null) {
        await referenciasApi.actualizar(referenciaEditando.id, form);
      } else {
        await referenciasApi.guardar(form);
      }

      // Cierra el modal y recarga la lista
      handleCerrarModal();
      cargarReferencias();

    } catch (err) {
      // Muestra el error del backend si existe
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al guardar la referencia');
      }
    }
  }

  // Activa o desactiva una referencia
  async function handleToggleActivo(referencia) {
    try {
      // Si esta activa la desactiva, si no la activa
      if (referencia.activo === 1) {
        await referenciasApi.desactivar(referencia.id);
      } else {
        await referenciasApi.activar(referencia.id);
      }
      // Recarga la lista
      cargarReferencias();
    } catch (err) {
      console.error('Error al cambiar estado de la referencia:', err);
    }
  }

  // Obtiene las marcas unicas para el filtro
  function obtenerMarcas() {
    // Crea un arreglo con las marcas sin repetir
    const marcas = [];
    for (let i = 0; i < referencias.length; i++) {
      if (marcas.includes(referencias[i].marca) === false) {
        marcas.push(referencias[i].marca);
      }
    }
    return marcas;
  }

  // Filtra las referencias segun busqueda y marca
  function referenciasFiltradas() {
    let resultado = referencias;

    // Filtra por marca si hay una seleccionada
    if (marcaFiltro !== '') {
      resultado = resultado.filter(function(ref) {
        return ref.marca === marcaFiltro;
      });
    }

    // Filtra por busqueda si hay texto
    if (busqueda.trim() !== '') {
      resultado = resultado.filter(function(ref) {
        const marcaCoincide = ref.marca.toLowerCase().includes(busqueda.toLowerCase());
        const refCoincide = ref.referencia.toLowerCase().includes(busqueda.toLowerCase());
        return marcaCoincide || refCoincide;
      });
    }

    return resultado;
  }

  return (
    <div>
      {/* Titulo de la pagina */}
      <h1 className="titulo-pagina">Referencias</h1>

      {/* Tabla de referencias */}
      <div className="tabla-contenedor">

        {/* Toolbar con busqueda, filtro de marca y boton nuevo */}
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
            + Nueva referencia
          </button>
        </div>

        {/* Tabla */}
        <table className="tabla">
          <thead>
            <tr>
              <th>Marca</th>
              <th>Referencia</th>
              <th>Descripción</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>

            {/* Estado de carga */}
            {cargando && (
              <tr>
                <td colSpan={5} className="tabla-vacia">Cargando...</td>
              </tr>
            )}

            {/* Sin referencias */}
            {cargando === false && referenciasFiltradas().length === 0 && (
              <tr>
                <td colSpan={5} className="tabla-vacia">No hay referencias registradas</td>
              </tr>
            )}

            {/* Filas de referencias */}
            {cargando === false && referenciasFiltradas().map(function(referencia) {
              return (
                <tr key={referencia.id}>
                  <td style={{ fontWeight: 500 }}>{referencia.marca}</td>
                  <td>{referencia.referencia}</td>
                  <td style={{ color: 'var(--gris-texto)' }}>{referencia.descripcion || '—'}</td>
                  <td>
                    {referencia.activo === 1
                      ? <span className="badge badge-exito">Activa</span>
                      : <span className="badge badge-peligro">Inactiva</span>
                    }
                  </td>
                  <td>
                    <div className="tabla-acciones">
                      {/* Boton editar */}
                      <button
                        className="tabla-btn-accion"
                        title="Editar"
                        onClick={function() { handleEditar(referencia); }}>
                        ✏️
                      </button>
                      {/* Boton activar o desactivar */}
                      <button
                        className="tabla-btn-accion"
                        title={referencia.activo === 1 ? 'Desactivar' : 'Activar'}
                        onClick={function() { handleToggleActivo(referencia); }}>
                        {referencia.activo === 1 ? '🔴' : '🟢'}
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
          <span>{referenciasFiltradas().length} referencia(s) encontrada(s)</span>
        </div>
      </div>

      {/* Modal de crear o editar referencia */}
      {mostrarModal && (
        <Modal
          titulo={referenciaEditando !== null ? 'Editar referencia' : 'Nueva referencia'}
          onCerrar={handleCerrarModal}>

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          {/* Campos marca y referencia en dos columnas */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Marca</label>
              <input
                type="text"
                placeholder="Ej: Levi's"
                value={form.marca}
                onChange={function(e) { handleCampo('marca', e.target.value); }}
              />
            </div>
            <div className="modal-campo">
              <label className="modal-label">Referencia</label>
              <input
                type="text"
                placeholder="Ej: 501"
                value={form.referencia}
                onChange={function(e) { handleCampo('referencia', e.target.value); }}
              />
            </div>
          </div>

          {/* Campo descripcion */}
          <div className="modal-campo">
            <label className="modal-label">Descripción (opcional)</label>
            <input
              type="text"
              placeholder="Ej: Jean clasico de corte recto"
              value={form.descripcion}
              onChange={function(e) { handleCampo('descripcion', e.target.value); }}
            />
          </div>

          {/* Botones del modal */}
          <div className="modal-footer">
            <button className="btn-secundario" onClick={handleCerrarModal}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleGuardar}>
              {referenciaEditando !== null ? 'Guardar cambios' : 'Crear referencia'}
            </button>
          </div>

        </Modal>
      )}
    </div>
  );
}