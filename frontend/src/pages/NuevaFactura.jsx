import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import facturasApi from '../api/facturasApi';
import facturaDetalleApi from '../api/facturaDetalleApi';
import clientesApi from '../api/clientesApi';
import vendedoresApi from '../api/vendedoresApi';
import inventarioApi from '../api/inventarioApi';
import { useAuth } from '../context/AuthContext';
import '../styles/tabla.css';

export default function NuevaFactura() {

  // Estado para los datos de la cabecera de la factura
  const [form, setForm] = useState({
   
    clienteId: '',
    vendedorId: '',
    fechaEmision: new Date().toISOString().split('T')[0],
    notas: ''
  });

  // Estado para la lista de clientes activos
  const [clientes, setClientes] = useState([]);

  // Estado para la lista de vendedores activos
  const [vendedores, setVendedores] = useState([]);

  // Estado para el inventario disponible
  const [inventario, setInventario] = useState([]);

  // Estado para la factura creada
  const [facturaCreada, setFacturaCreada] = useState(null);

  // Estado para los items agregados a la factura
  const [items, setItems] = useState([]);

  // Estado para el item que se esta agregando
  const [itemForm, setItemForm] = useState({
    inventarioId: '',
    cantidad: '',
    precioUnitario: ''
  });

  // Estado del paso actual: 1=cabecera, 2=productos
  const [paso, setPaso] = useState(1);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Estado para mensajes de error del item
  const [errorItem, setErrorItem] = useState('');

  // Obtiene el usuario y funciones del contexto
  const { esAdmin, usuario } = useAuth();

  // Hook para navegar a otras paginas
  const navigate = useNavigate();

  // Carga los datos al montar el componente
  useEffect(function() {
    cargarDatos();
  }, []);

  // Cuando se selecciona un producto actualiza el precio automaticamente
  useEffect(function() {
    // Si hay un inventario seleccionado busca su precio
    if (itemForm.inventarioId !== '') {
      const itemSeleccionado = inventario.find(function(item) {
        return item.id === Number(itemForm.inventarioId);
      });

      // Si lo encuentra actualiza el precio unitario
      if (itemSeleccionado) {
        setItemForm(function(prev) {
          return { ...prev, precioUnitario: itemSeleccionado.precio };
        });
      }
    }
  }, [itemForm.inventarioId]);

  // Carga clientes, vendedores e inventario del backend
  async function cargarDatos() {
    setCargando(true);
    try {
      // Carga clientes activos
      const respuestaClientes = await clientesApi.listarActivos();
      setClientes(respuestaClientes.data);

      // Carga vendedores activos
      const respuestaVendedores = await vendedoresApi.listarActivos();
      setVendedores(respuestaVendedores.data);

      // Carga inventario con stock disponible
      const respuestaInventario = await inventarioApi.listarConStock();
      setInventario(respuestaInventario.data);

      // Si es vendedor preselecciona su vendedorId
      if (esAdmin() === false) {
        setForm(function(prev) {
          return { ...prev, vendedorId: usuario.vendedorId };
        });
      }

    } catch (err) {
      console.error('Error al cargar datos:', err);
    } finally {
      setCargando(false);
    }
  }

  // Actualiza un campo del formulario de cabecera
  function handleCampo(campo, valor) {
    setForm({ ...form, [campo]: valor });
  }

  // Actualiza un campo del formulario de item
  function handleCampoItem(campo, valor) {
    setItemForm({ ...itemForm, [campo]: valor });
  }

  // Crea la factura y pasa al paso 2
  async function handleCrearFactura() {
    // Valida campos obligatorios
   

    if (form.clienteId === '') {
      setError('El cliente es obligatorio');
      return;
    }

    if (form.vendedorId === '') {
      setError('El vendedor es obligatorio');
      return;
    }

    if (form.fechaEmision === '') {
      setError('La fecha de emision es obligatoria');
      return;
    }

    setError('');

    // Arma el request de la factura
    const request = {
    
      clienteId: Number(form.clienteId),
      vendedorId: Number(form.vendedorId),
      fechaEmision: form.fechaEmision,
      notas: form.notas
    };

    try {
      // Crea la factura en el backend
      const respuesta = await facturasApi.guardar(request);

      // Guarda la factura creada
      setFacturaCreada(respuesta.data);

      // Pasa al paso 2
      setPaso(2);

    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al crear la factura');
      }
    }
  }

  // Agrega un item a la factura
  async function handleAgregarItem() {
    // Valida campos obligatorios del item
    if (itemForm.inventarioId === '') {
      setErrorItem('El producto es obligatorio');
      return;
    }

    if (itemForm.cantidad === '' || Number(itemForm.cantidad) <= 0) {
      setErrorItem('La cantidad debe ser mayor a cero');
      return;
    }

    if (itemForm.precioUnitario === '' || Number(itemForm.precioUnitario) <= 0) {
      setErrorItem('El precio debe ser mayor a cero');
      return;
    }

    setErrorItem('');

    // Arma el request del item
    const request = {
      facturaId: facturaCreada.id,
      inventarioId: Number(itemForm.inventarioId),
      cantidad: Number(itemForm.cantidad),
      precioUnitario: Number(itemForm.precioUnitario)
    };

    try {
      // Agrega el item a la factura
      const respuesta = await facturaDetalleApi.agregar(request);

      // Agrega el item a la lista local
      setItems(function(prev) {
        return [...prev, respuesta.data];
      });

      // Limpia el formulario del item
      setItemForm({ inventarioId: '', cantidad: '', precioUnitario: '' });

      // Recarga el inventario para actualizar el stock disponible
      const respuestaInventario = await inventarioApi.listarConStock();
      setInventario(respuestaInventario.data);

    } catch (err) {
      if (err.response && err.response.data) {
        setErrorItem(err.response.data);
      } else {
        setErrorItem('Error al agregar el producto');
      }
    }
  }

  // Elimina un item de la factura
  async function handleEliminarItem(item) {
    try {
      // Elimina el item del backend
      await facturaDetalleApi.eliminar(item.id);

      // Elimina el item de la lista local
      setItems(function(prev) {
        return prev.filter(function(i) {
          return i.id !== item.id;
        });
      });

      // Recarga el inventario para devolver el stock
      const respuestaInventario = await inventarioApi.listarConStock();
      setInventario(respuestaInventario.data);

    } catch (err) {
      console.error('Error al eliminar item:', err);
    }
  }

  // Calcula el subtotal de todos los items
  function calcularSubtotal() {
    let subtotal = 0;
    for (let i = 0; i < items.length; i++) {
      subtotal = subtotal + (Number(items[i].precioUnitario) * Number(items[i].cantidad));
    }
    return subtotal;
  }

  // Formatea un numero como moneda colombiana
  function formatearMoneda(valor) {
    return '$' + Number(valor).toLocaleString('es-CO');
  }

  // Finaliza la factura y redirige a la lista
  function handleFinalizar() {
    navigate('/facturas');
  }

  // Muestra cargando mientras se obtienen los datos
  if (cargando) {
    return <div style={{ padding: 32, color: 'var(--gris-texto)' }}>Cargando...</div>;
  }

  return (
    <div>

      {/* Titulo y boton volver */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 24 }}>
        <button
          className="btn-secundario"
          onClick={function() { navigate('/facturas'); }}>
          ← Volver
        </button>
        <h1 className="titulo-pagina" style={{ marginBottom: 0 }}>Nueva factura</h1>
      </div>

      {/* Indicador de pasos */}
      <div style={{ display: 'flex', gap: 8, marginBottom: 32 }}>
        <div style={{
          padding: '8px 20px',
          borderRadius: 8,
          fontSize: 13,
          fontWeight: 500,
          background: paso === 1 ? 'var(--rojo-principal)' : 'var(--fondo-terciario)',
          color: paso === 1 ? '#fff' : 'var(--gris-texto)'
        }}>
          1. Datos de la factura
        </div>
        <div style={{
          padding: '8px 20px',
          borderRadius: 8,
          fontSize: 13,
          fontWeight: 500,
          background: paso === 2 ? 'var(--rojo-principal)' : 'var(--fondo-terciario)',
          color: paso === 2 ? '#fff' : 'var(--gris-texto)'
        }}>
          2. Agregar productos
        </div>
      </div>

      {/* PASO 1 - Datos de la factura */}
      {paso === 1 && (
        <div className="card">

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

         

          {/* Campos cliente y vendedor */}
          <div className="modal-grid-2">
            <div className="modal-campo">
              <label className="modal-label">Cliente</label>
              <select
                value={form.clienteId}
                onChange={function(e) { handleCampo('clienteId', e.target.value); }}>
                <option value="">Selecciona un cliente</option>
                {clientes.map(function(cliente) {
                  return (
                    <option key={cliente.id} value={cliente.id}>
                      {cliente.nombre} — {cliente.nombreAlmacen}
                    </option>
                  );
                })}
              </select>
            </div>

            <div className="modal-campo">
              <label className="modal-label">Vendedor</label>
              {/* Si es vendedor muestra su nombre, si es admin puede seleccionar */}
              {esAdmin() ? (
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
              ) : (
                <div style={{ padding: '10px 14px', background: 'var(--fondo-terciario)', borderRadius: 8, color: 'var(--gris-texto)', fontSize: 14 }}>
                  {vendedores.find(function(v) { return v.id === usuario.vendedorId; })
                    ? vendedores.find(function(v) { return v.id === usuario.vendedorId; }).nombre
                    : 'Cargando...'}
                </div>
              )}
            </div>
          </div>

          {/* Campo fecha de emision */}
          <div className="modal-campo">
            <label className="modal-label">Fecha de emisión</label>
            <input
              type="date"
              value={form.fechaEmision}
              onChange={function(e) { handleCampo('fechaEmision', e.target.value); }}
            />
          </div>

          {/* Campo notas */}
          <div className="modal-campo">
            <label className="modal-label">Notas (opcional)</label>
            <input
              type="text"
              placeholder="Observaciones de la factura"
              value={form.notas}
              onChange={function(e) { handleCampo('notas', e.target.value); }}
            />
          </div>

          {/* Boton continuar */}
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 8 }}>
            <button className="btn-primario" onClick={handleCrearFactura}>
              Continuar → Agregar productos
            </button>
          </div>
        </div>
      )}

      {/* PASO 2 - Agregar productos */}
      {paso === 2 && facturaCreada !== null && (
        <div>

          {/* Resumen de la factura creada */}
          <div className="card" style={{ marginBottom: 16 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 12 }}>
              <div>
                <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Factura</div>
                <div style={{ fontWeight: 600, fontSize: 16 }}>{facturaCreada.numeroFactura}</div>
              </div>
              <div>
                <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Cliente</div>
                <div style={{ fontSize: 14 }}>{facturaCreada.clienteNombre}</div>
                <div style={{ fontSize: 12, color: 'var(--gris-texto)' }}>{facturaCreada.clienteAlmacen}</div>
              </div>
              <div>
                <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Vendedor</div>
                <div style={{ fontSize: 14 }}>{facturaCreada.vendedorNombre}</div>
              </div>
              <div>
                <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Subtotal actual</div>
                <div style={{ fontWeight: 600, fontSize: 16 }}>{formatearMoneda(calcularSubtotal())}</div>
              </div>
            </div>
          </div>

          {/* Formulario para agregar un producto */}
          <div className="card">
            <div style={{ fontSize: 15, fontWeight: 600, marginBottom: 16 }}>Agregar producto</div>

            {/* Mensaje de error del item */}
            {errorItem !== '' && (
              <div className="error-mensaje">{errorItem}</div>
            )}

            {/* Campos del item en tres columnas */}
            <div className="modal-grid-3">
              <div className="modal-campo">
                <label className="modal-label">Producto</label>
                <select
                  value={itemForm.inventarioId}
                  onChange={function(e) { handleCampoItem('inventarioId', e.target.value); }}>
                  <option value="">Selecciona un producto</option>
                  {inventario.map(function(item) {
                    return (
                      <option key={item.id} value={item.id}>
                        {item.marca} {item.referencia} — T{item.talla} ({item.cantidad} uds)
                      </option>
                    );
                  })}
                </select>
              </div>

              <div className="modal-campo">
                <label className="modal-label">Cantidad</label>
                <input
                  type="number"
                  min="1"
                  placeholder="0"
                  value={itemForm.cantidad}
                  onChange={function(e) { handleCampoItem('cantidad', e.target.value); }}
                />
              </div>

              <div className="modal-campo">
                <label className="modal-label">Precio unitario ($)</label>
                <input
                  type="number"
                  min="0"
                  placeholder="0"
                  value={itemForm.precioUnitario}
                  onChange={function(e) { handleCampoItem('precioUnitario', e.target.value); }}
                />
              </div>
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
              <button className="btn-primario" onClick={handleAgregarItem}>
                + Agregar producto
              </button>
            </div>
          </div>

          {/* Tabla de items agregados */}
          <div className="tabla-contenedor" style={{ marginTop: 16 }}>
            <table className="tabla">
              <thead>
                <tr>
                  <th>Producto</th>
                  <th>Talla</th>
                  <th>Cantidad</th>
                  <th>Precio unitario</th>
                  <th>Subtotal</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>

                {/* Sin items */}
                {items.length === 0 && (
                  <tr>
                    <td colSpan={6} className="tabla-vacia">
                      Aun no has agregado productos a esta factura
                    </td>
                  </tr>
                )}

                {/* Filas de items */}
                {items.map(function(item) {
                  return (
                    <tr key={item.id}>
                      <td>
                        <div style={{ fontWeight: 500 }}>{item.marca}</div>
                        <div style={{ fontSize: 12, color: 'var(--gris-texto)' }}>{item.referencia}</div>
                      </td>
                      <td>{item.talla}</td>
                      <td>{item.cantidad}</td>
                      <td>{formatearMoneda(item.precioUnitario)}</td>
                      <td style={{ fontWeight: 500 }}>
                        {formatearMoneda(Number(item.precioUnitario) * Number(item.cantidad))}
                      </td>
                      <td>
                        {/* Boton eliminar item */}
                        <button
                          className="tabla-btn-accion"
                          title="Eliminar producto"
                          onClick={function() { handleEliminarItem(item); }}>
                          🗑️
                        </button>
                      </td>
                    </tr>
                  );
                })}

                {/* Fila de total */}
                {items.length > 0 && (
                  <tr style={{ background: 'var(--fondo-terciario)' }}>
                    <td colSpan={4} style={{ textAlign: 'right', fontWeight: 600, padding: '12px 16px' }}>
                      Total
                    </td>
                    <td style={{ fontWeight: 700, fontSize: 15, padding: '12px 16px' }}>
                      {formatearMoneda(calcularSubtotal())}
                    </td>
                    <td></td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* Botones finales */}
          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 24 }}>
            <div style={{ fontSize: 13, color: 'var(--gris-texto)', alignSelf: 'center' }}>
              {items.length} producto(s) agregado(s)
            </div>
            <div style={{ display: 'flex', gap: 10 }}>
              <button
                className="btn-secundario"
                onClick={function() { navigate('/facturas'); }}>
                Guardar y salir
              </button>
              <button
                className="btn-primario"
                disabled={items.length === 0}
                onClick={handleFinalizar}>
                Finalizar factura ✓
              </button>
            </div>
          </div>

        </div>
      )}
    </div>
  );
}