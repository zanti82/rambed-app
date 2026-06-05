import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from '../components/Modal';
import facturasApi from '../api/facturasApi';
import facturaDetalleApi from '../api/facturaDetalleApi';
import { useAuth } from '../context/AuthContext';
import '../styles/tabla.css';
import FacturaPrint from '../components/FacturaPrint';

export default function Facturas() {

  // Estado para la lista de facturas
  const [facturas, setFacturas] = useState([]);

  // Estado para los items de la factura seleccionada
  const [itemsFactura, setItemsFactura] = useState([]);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Estado para mostrar el modal de detalle
  const [mostrarDetalle, setMostrarDetalle] = useState(false);

  // Estado para mostrar el modal de pago
  const [mostrarPago, setMostrarPago] = useState(false);

  // Estado para la factura seleccionada
  const [facturaSeleccionada, setFacturaSeleccionada] = useState(null);

  // Estado para el buscador
  const [busqueda, setBusqueda] = useState('');

  // Estado para el filtro de estado
  const [estadoFiltro, setEstadoFiltro] = useState('');

  // Estado para mensajes de error
  const [error, setError] = useState('');

  // Estado para el descuento del pago
  const [descuento, setDescuento] = useState('');

  // Estado de carga del detalle
  const [cargandoDetalle, setCargandoDetalle] = useState(false);

  // Obtiene el usuario y funciones del contexto
  const { esAdmin, usuario } = useAuth();

  // Estado para mostrar la vista de impresion
  const [mostrarPrint, setMostrarPrint] = useState(false);

  // Estado para los items de impresion
  const [itemsPrint, setItemsPrint] = useState([]);

  // Estado para la factura a imprimir
  const [facturaImprimir, setFacturaImprimir] = useState(null);

  // Estado para mostrar el modal de abonos
const [mostrarAbonos, setMostrarAbonos] = useState(false);

// Estado para la lista de abonos de la factura seleccionada
const [abonos, setAbonos] = useState([]);

// Estado para el monto del abono nuevo
const [montoAbono, setMontoAbono] = useState('');

// Estado para la fecha del abono nuevo
const [fechaAbono, setFechaAbono] = useState('');

// Estado para las notas del abono nuevo
const [notasAbono, setNotasAbono] = useState('');

// Estado para el porcentaje de comision de pago
const [porcComisionPago, setPorcComisionPago] = useState('4');

// Estado de carga de abonos
const [cargandoAbonos, setCargandoAbonos] = useState(false);

  // Hook para navegar a otras paginas
  const navigate = useNavigate();

  // Carga las facturas al montar el componente
  useEffect(function() {
    cargarFacturas();
  }, []);

  // Obtiene las facturas del backend
  async function cargarFacturas() {
    setCargando(true);
    try {
      // Si es admin carga todas las facturas
      // Si es vendedor carga solo las suyas
      let respuesta = null;
      if (esAdmin()) {
        respuesta = await facturasApi.listarTodas();
      } else {
        respuesta = await facturasApi.listarPorVendedor(usuario.vendedorId);
      }
      setFacturas(respuesta.data);
    } catch (err) {
      console.error('Error al cargar facturas:', err);
    } finally {
      setCargando(false);
    }
  }

  // Abre el modal de detalle de una factura
  async function handleVerDetalle(factura) {
    // Guarda la factura seleccionada
    setFacturaSeleccionada(factura);
    // Limpia los items anteriores
    setItemsFactura([]);
    // Abre el modal
    setMostrarDetalle(true);
    // Activa la carga del detalle
    setCargandoDetalle(true);

    try {
      // Carga los items de la factura
      const respuesta = await facturaDetalleApi.listarPorFactura(factura.id);
      setItemsFactura(respuesta.data);
    } catch (err) {
      console.error('Error al cargar detalle:', err);
    } finally {
      setCargandoDetalle(false);
    }
  }

  // Abre el modal de pago
  function handleAbrirPago(factura) {
    // Guarda la factura seleccionada
    setFacturaSeleccionada(factura);
    // Limpia el descuento
    setDescuento('');
    // Limpia el error
    setError('');
    // Abre el modal de pago
    setMostrarPago(true);
  }

  // Registra el pago de una factura
  async function handleRegistrarPago() {
    setError('');

    try {
      // Arma el request con el descuento opcional
      const pagoRequest = {
        descuentoPorcentaje: descuento !== '' ? Number(descuento) : 0
      };

      console.log("abriendo modal con:"+pagoRequest)

      // Registra el pago en el backend
      await facturasApi.registrarPago(facturaSeleccionada.id, pagoRequest);

      // Cierra el modal y recarga la lista
      setMostrarPago(false);
      setFacturaSeleccionada(null);
      cargarFacturas();

    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Error al registrar el pago');
      }
    }
  }

  // Anula una factura
  async function handleAnular(factura) {
    // Pide confirmacion antes de anular
    const confirmar = window.confirm('¿Estas seguro de anular la factura ' + factura.numeroFactura + '?');

    if (confirmar === false) {
      return;
    }

    try {
      await facturasApi.anular(factura.id);
      cargarFacturas();
    } catch (err) {
      console.error('Error al anular factura:', err);
    }
  }

  // Filtra las facturas segun busqueda y estado
  function facturasFiltradas() {
    let resultado = facturas;

    // Filtra por estado si hay uno seleccionado
    if (estadoFiltro !== '') {
      resultado = resultado.filter(function(factura) {
        return factura.estado === estadoFiltro;
      });
    }

    // Filtra por busqueda si hay texto
    if (busqueda.trim() !== '') {
      resultado = resultado.filter(function(factura) {
        const numCoincide = factura.numeroFactura.toLowerCase().includes(busqueda.toLowerCase());
        const clienteCoincide = factura.clienteNombre.toLowerCase().includes(busqueda.toLowerCase());
        const almacenCoincide = factura.clienteAlmacen.toLowerCase().includes(busqueda.toLowerCase());
        return numCoincide || clienteCoincide || almacenCoincide;
      });
    }

    return resultado;
  }

  // Retorna la clase del badge segun el estado de la factura
  function obtenerClaseBadge(estado) {
    if (estado === 'pagada') {
      return 'badge badge-exito';
    }
    if (estado === 'anulada') {
      return 'badge badge-peligro';
    }
    return 'badge badge-pendiente';
  }

  // Formatea un numero como moneda colombiana
  function formatearMoneda(valor) {
    return '$' + Number(valor).toLocaleString('es-CO');
  }

  // Calcula el subtotal de un item de detalle
  function calcularSubtotalItem(item) {
    return Number(item.precioUnitario) * Number(item.cantidad);
  }

  

  // Abre la vista de impresion y carga los items
  async function handleImprimir(factura) {
    // Guarda la factura a imprimir
    
    setFacturaImprimir(factura);

    // Limpia los items anteriores
    setItemsPrint([]);

    try {
      // Carga los items de la factura
      const respuesta = await facturaDetalleApi.listarPorFactura(factura.id);
      console.log(respuesta.data)
      setItemsPrint(respuesta.data);

      // Activa la vista de impresion
      setMostrarPrint(true);

      // Espera que el DOM se actualice y lanza la impresion
      //setTimeout(function() {
       // window.print();
      //  setMostrarPrint(false);
      //}, 5000);

    } catch (err) {
      console.error('Error al cargar items para impresion:', err);
    }
  }

  // Imprime la factura en una ventana nueva
function handleImprimirVentana() {
  // Obtiene el HTML del componente de factura
  const contenido = document.getElementById('factura-print').innerHTML;

  // Abre una ventana nueva
  const ventana = window.open('', '_blank', 'width=900,height=700');

  // Escribe el HTML de la factura en la ventana nueva
  ventana.document.write(`
    <!DOCTYPE html>
    <html>
      <head>
        <title>${facturaImprimir.numeroFactura}</title>
        <style>
          body { margin: 0; padding: 0; font-family: Arial, sans-serif; }
          * { box-sizing: border-box; }
        </style>
      </head>
      <body>
        ${contenido}
      </body>
    </html>
  `);

  // Cierra el documento para que cargue bien
  ventana.document.close();

  // Espera que cargue y lanza la impresion
  ventana.onload = function() {
    ventana.print();
  };
}
// Abre el modal de abonos y carga los abonos existentes
async function handleAbonos(factura) {
    // Guarda la factura seleccionada
    setFacturaSeleccionada(factura);
    // Limpia los campos
    setMontoAbono('');
    setFechaAbono('');
    setNotasAbono('');
    setError('');
    // Abre el modal
    setMostrarAbonos(true);
    // Activa la carga
    setCargandoAbonos(true);

    try {
        // Carga los abonos existentes de la factura
        const respuesta = await facturasApi.listarAbonos(factura.id);
        setAbonos(respuesta.data);
    } catch (err) {
        console.error('Error al cargar abonos:', err);
    } finally {
        setCargandoAbonos(false);
    }
  }

// Registra un abono parcial
async function handleRegistrarAbono() {
  setError('');

  // Valida que venga el monto
  if (montoAbono === '' || Number(montoAbono) <= 0) {
      setError('El monto del abono es obligatorio');
      return;
  }

  // Valida que venga la fecha
  if (fechaAbono === '') {
      setError('La fecha del abono es obligatoria');
      return;
  }

  try {
      // Arma el request del abono
      const abonoRequest = {
          facturaId: facturaSeleccionada.id,
          monto: Number(montoAbono),
          fechaPago: fechaAbono,
          notas: notasAbono !== '' ? notasAbono : null
      };

      // Registra el abono en el backend
      await facturasApi.registrarAbono(abonoRequest);

      // Limpia los campos del formulario
      setMontoAbono('');
      setFechaAbono('');
      setNotasAbono('');

      // Recarga los abonos y las facturas
      const respuesta = await facturasApi.listarAbonos(facturaSeleccionada.id);
      setAbonos(respuesta.data);
      cargarFacturas();

  } catch (err) {
      if (err.response && err.response.data) {
          setError(err.response.data);
      } else {
          setError('Error al registrar el abono');
      }
  }
}

async function handleMarcarPag() {
  setError('');
  
  try{

  }catch{
    
  }

  
}
// Marca la factura como pagada con comision de pago
async function handleMarcarPagada() {
  setError('');

  try {
      // Arma el request con descuento y comision de pago
      const pagoRequest = {
          descuentoPorcentaje: descuento !== '' ? Number(descuento) : 0,
          porcComisionPago: porcComisionPago !== '' ? Number(porcComisionPago) : 4
      };

      console.log("este el pago:" +pagoRequest)
      // Registra el pago en el backend
      await facturasApi.registrarPago(facturaSeleccionada.id, pagoRequest);

      // Cierra los modales y recarga
      setMostrarAbonos(false);
      setMostrarPago(false);
      setFacturaSeleccionada(null);
      cargarFacturas();

  } catch (err) {
      if (err.response && err.response.data) {
          setError(err.response.data);
      } else {
          setError('Error al marcar como pagada');
      }
  }
}


  return (
    <div>
      {/* Titulo y boton nueva factura */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h1 className="titulo-pagina" style={{ marginBottom: 0 }}>Facturas</h1>
        <button className="btn-primario" onClick={function() { navigate('/facturas/nueva'); }}>
          + Nueva factura
        </button>
      </div>

      {/* Tabla de facturas */}
      <div className="tabla-contenedor">

        {/* Toolbar con busqueda y filtro de estado */}
        <div className="tabla-toolbar">
          <input
            className="tabla-busqueda"
            placeholder="Buscar por numero, cliente o almacen..."
            value={busqueda}
            onChange={function(e) { setBusqueda(e.target.value); }}
          />

          {/* Filtro por estado */}
          <select
            style={{ width: 'auto' }}
            value={estadoFiltro}
            onChange={function(e) { setEstadoFiltro(e.target.value); }}>
            <option value="">Todos los estados</option>
            <option value="pendiente">Pendiente</option>
            <option value="pagada">Pagada</option>
            <option value="anulada">Anulada</option>
          </select>
        </div>

        {/* Tabla */}
        <table className="tabla">
          <thead>
            <tr>
              <th>N° Factura</th>
              <th>Cliente</th>
              <th>Vendedor</th>
              <th>Fecha emisión</th>
              <th>Subtotal</th>
              <th>Total</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>

            {/* Estado de carga */}
            {cargando && (
              <tr>
                <td colSpan={8} className="tabla-vacia">Cargando...</td>
              </tr>
            )}

            {/* Sin facturas */}
            {cargando === false && facturasFiltradas().length === 0 && (
              <tr>
                <td colSpan={8} className="tabla-vacia">No hay facturas registradas</td>
              </tr>
            )}

            {/* Filas de facturas */}
            {cargando === false && facturasFiltradas().map(function(factura) {
              return (
                <tr key={factura.id}>
                  <td style={{ fontWeight: 500 }}>
                    {factura.numeroFactura}
                    {/* Muestra un indicador si la factura tiene abonos */}
                    {Number(factura.totalPagado) > 0 && (
                      <span style={{ marginLeft: 6, fontSize: 11, color: 'var(--amarillo-texto)', background: 'var(--amarillo-fondo)', padding: '2px 6px', borderRadius: 4 }}>
                        Abonado {formatearMoneda(factura.totalPagado)}
                      </span>
                    )}
                </td>
                  <td>
                    <div>{factura.clienteNombre}</div>
                    <div style={{ fontSize: 12, color: 'var(--gris-texto)' }}>{factura.clienteAlmacen}</div>
                  </td>
                  <td>{factura.vendedorNombre}</td>
                  <td>{factura.fechaEmision}</td>
                  <td>{formatearMoneda(factura.subtotal)}</td>
                  <td style={{ fontWeight: 500 }}>{formatearMoneda(factura.total)}</td>
                  <td>
                    <span className={obtenerClaseBadge(factura.estado)}>
                      {factura.estado}
                    </span>
                  </td>
                  <td>
                    <div className="tabla-acciones">
                      {/* Boton ver detalle */}
                      <button
                        className="tabla-btn-accion"
                        title="Ver detalle"
                        onClick={function() { handleVerDetalle(factura); }}>
                        👁️
                      </button>

                      {/* Boton registrar pago solo si esta pendiente */}
                      {factura.estado === 'pendiente' && (
                        <button
                          className="tabla-btn-accion"
                          title="Registrar pago"
                          onClick={function() { handleAbrirPago(factura); }}>
                          💰
                        </button>
                      )}
                      {/* Boton abonos solo si esta pendiente */}
                      {factura.estado === 'pendiente' && (
                          <button
                              className="tabla-btn-accion"
                              title="Abonos"
                              onClick={function() { handleAbonos(factura); }}>
                              💵
                          </button>
                      )}

                      {/* Boton anular solo si no esta anulada y es admin */}
                      {factura.estado !== 'anulada' && esAdmin() && (
                        <button
                          className="tabla-btn-accion"
                          title="Anular factura"
                          onClick={function() { handleAnular(factura); }}>
                          🚫
                        </button>
                      )}

                      {/* Boton imprimir */}
                      <button
                        className="tabla-btn-accion"
                        title="Imprimir factura"
                        onClick={function() { handleImprimir(factura); }}>
                        🖨️
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
          <span>{facturasFiltradas().length} factura(s) encontrada(s)</span>
        </div>
      </div>

      {/* Modal de detalle de factura */}
      {mostrarDetalle && facturaSeleccionada !== null && (
        <Modal
          titulo={'Detalle — ' + facturaSeleccionada.numeroFactura}
          onCerrar={function() { setMostrarDetalle(false); setFacturaSeleccionada(null); }}>

          {/* Informacion general de la factura */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 20 }}>
            <div>
              <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Cliente</div>
              <div style={{ fontSize: 14, fontWeight: 500 }}>{facturaSeleccionada.clienteNombre}</div>
              <div style={{ fontSize: 12 }}>{facturaSeleccionada.clienteAlmacen}</div>
            </div>
            <div>
              <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Vendedor</div>
              <div style={{ fontSize: 14 }}>{facturaSeleccionada.vendedorNombre}</div>
            </div>
            <div>
              <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Fecha emisión</div>
              <div style={{ fontSize: 14 }}>{facturaSeleccionada.fechaEmision}</div>
            </div>
            <div>
              <div style={{ fontSize: 11, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 4 }}>Estado</div>
              <span className={obtenerClaseBadge(facturaSeleccionada.estado)}>
                {facturaSeleccionada.estado}
              </span>
            </div>
          </div>

          {/* Tabla de items */}
          <div style={{ border: '1px solid var(--borde)', borderRadius: 8, overflow: 'hidden', marginBottom: 16 }}>
            <table className="tabla">
              <thead>
                <tr>
                  <th>Producto</th>
                  <th>Talla</th>
                  <th>Cant.</th>
                  <th>Precio</th>
                  <th>Subtotal</th>
                </tr>
              </thead>
              <tbody>
                {/* Estado de carga del detalle */}
                {cargandoDetalle && (
                  <tr>
                    <td colSpan={5} className="tabla-vacia">Cargando items...</td>
                  </tr>
                )}

                {/* Sin items */}
                {cargandoDetalle === false && itemsFactura.length === 0 && (
                  <tr>
                    <td colSpan={5} className="tabla-vacia">Sin productos</td>
                  </tr>
                )}

                {/* Filas de items */}
                {cargandoDetalle === false && itemsFactura.map(function(item) {
                  return (
                    <tr key={item.id}>
                      <td>
                        <div style={{ fontWeight: 500 }}>{item.marca}</div>
                        <div style={{ fontSize: 12, color: 'var(--gris-texto)' }}>{item.referencia}</div>
                      </td>
                      <td>{item.talla}</td>
                      <td>{item.cantidad}</td>
                      <td>{formatearMoneda(item.precioUnitario)}</td>
                      <td>{formatearMoneda(calcularSubtotalItem(item))}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>

          {/* Totales de la factura */}
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 8 }}>
            <div style={{ display: 'flex', gap: 24 }}>
              <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Subtotal</span>
              <span style={{ fontSize: 13 }}>{formatearMoneda(facturaSeleccionada.subtotal)}</span>
            </div>
            {Number(facturaSeleccionada.descuentoPorcentaje) > 0 && (
              <div style={{ display: 'flex', gap: 24 }}>
                <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Descuento ({facturaSeleccionada.descuentoPorcentaje}%)</span>
                <span style={{ fontSize: 13, color: 'var(--exito-texto)' }}>
                  -{formatearMoneda(facturaSeleccionada.subtotal - facturaSeleccionada.total)}
                </span>
              </div>
            )}
            <div style={{ display: 'flex', gap: 24, borderTop: '1px solid var(--borde)', paddingTop: 8 }}>
              <span style={{ fontWeight: 600, fontSize: 15 }}>Total</span>
              <span style={{ fontWeight: 600, fontSize: 15 }}>{formatearMoneda(facturaSeleccionada.total)}</span>
            </div>
          </div>

          {/* Notas si existen */}
          {facturaSeleccionada.notas && (
            <div style={{ marginTop: 16, padding: 12, background: 'var(--fondo-terciario)', borderRadius: 8, fontSize: 13, color: 'var(--gris-texto)' }}>
              <strong>Notas:</strong> {facturaSeleccionada.notas}
            </div>
          )}

        </Modal>
      )}

      {/* Modal de registrar pago */}
      {mostrarPago && facturaSeleccionada !== null && (
        <Modal
          titulo={'Registrar pago — ' + facturaSeleccionada.numeroFactura}
          onCerrar={function() { setMostrarPago(false); setFacturaSeleccionada(null); }}>

          {/* Mensaje de error */}
          {error !== '' && (
            <div className="error-mensaje">{error}</div>
          )}

          {/* Resumen de la factura */}
          <div style={{ padding: 16, background: 'var(--fondo-terciario)', borderRadius: 8, marginBottom: 20 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
              <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Cliente</span>
              <span style={{ fontSize: 13 }}>{facturaSeleccionada.clienteNombre}</span>
             
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Subtotal a cobrar</span>
              <span style={{ fontSize: 15, fontWeight: 600 }}>{formatearMoneda(facturaSeleccionada.subtotal)}</span>
            </div>
          </div>

          {/* Campo descuento opcional */}
          <div className="modal-campo">
            <label className="modal-label">Descuento (%) — opcional</label>
            <input
              type="number"
              min="0"
              max="100"
              placeholder="Ej: 10 para un 10% de descuento"
              value={descuento}
              onChange={function(e) { setDescuento(e.target.value); }}
            />
          </div>

          {/* Preview del total con descuento */}
          {descuento !== '' && Number(descuento) > 0 && (
            <div style={{ padding: 12, background: 'var(--exito-fondo)', borderRadius: 8, marginBottom: 16 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
                <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Descuento ({descuento}%)</span>
                <span style={{ fontSize: 13, color: 'var(--exito-texto)' }}>
                  -{formatearMoneda(facturaSeleccionada.subtotal * Number(descuento) / 100)}
                </span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ fontWeight: 600, fontSize: 14 }}>Total a pagar</span>
                <span style={{ fontWeight: 600, fontSize: 14, color: 'var(--exito-texto)' }}>
                  {formatearMoneda(facturaSeleccionada.subtotal - (facturaSeleccionada.subtotal * Number(descuento) / 100))}
                </span>
              </div>
            </div>
          )}

          {/* Botones del modal */}
          <div className="modal-footer">
            <button
              className="btn-secundario"
              onClick={function() { setMostrarPago(false); setFacturaSeleccionada(null); }}>
              Cancelar
            </button>
            <button className="btn-primario" onClick={handleRegistrarPago}>
              Confirmar pago
            </button>
          </div>

        </Modal>
      )}

      {/* Modal de abonos */}
        {mostrarAbonos && facturaSeleccionada !== null && (
            <Modal
                titulo={'Abonos — ' + facturaSeleccionada.numeroFactura}
                onCerrar={function() { setMostrarAbonos(false); setFacturaSeleccionada(null); }}>

                {/* Error */}
                {error !== '' && (
                    <div className="error-mensaje">{error}</div>
                )}

                {/* Resumen de la factura */}
                <div style={{ padding: 16, background: 'var(--fondo-terciario)', borderRadius: 8, marginBottom: 20 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
                        <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Total factura</span>
                        <span style={{ fontSize: 13, fontWeight: 600 }}>{formatearMoneda(facturaSeleccionada.subtotal)}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                        <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Total abonado</span>
                        <span style={{ fontSize: 13, color: 'var(--exito-texto)' }}>{formatearMoneda(facturaSeleccionada.totalPagado)}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 8, borderTop: '1px solid var(--borde)', paddingTop: 8 }}>
                        <span style={{ color: 'var(--gris-texto)', fontSize: 13 }}>Saldo pendiente</span>
                        <span style={{ fontSize: 14, fontWeight: 600, color: 'var(--rojo-principal)' }}>
                            {formatearMoneda(facturaSeleccionada.subtotal - facturaSeleccionada.totalPagado)}
                        </span>
                    </div>
                </div>

                {/* Historial de abonos */}
                <div style={{ marginBottom: 20 }}>
                    <div style={{ fontSize: 12, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 8 }}>Historial de abonos</div>
                    {cargandoAbonos && (
                        <div style={{ fontSize: 13, color: 'var(--gris-texto)' }}>Cargando...</div>
                    )}
                    {cargandoAbonos === false && abonos.length === 0 && (
                        <div style={{ fontSize: 13, color: 'var(--gris-texto)' }}>Sin abonos registrados</div>
                    )}
                    {cargandoAbonos === false && abonos.map(function(abono) {
                        return (
                            <div key={abono.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--borde)', fontSize: 13 }}>
                                <span style={{ color: 'var(--gris-texto)' }}>{abono.fechaPago}</span>
                                <span style={{ fontWeight: 500, color: 'var(--exito-texto)' }}>{formatearMoneda(abono.monto)}</span>
                                {abono.notas && <span style={{ color: 'var(--gris-texto)', fontSize: 12 }}>{abono.notas}</span>}
                            </div>
                        );
                    })}
                </div>

                {/* Formulario nuevo abono */}
                <div style={{ fontSize: 12, color: 'var(--gris-texto)', textTransform: 'uppercase', marginBottom: 12 }}>Registrar abono</div>
                <div className="modal-campo">
                    <label className="modal-label">Monto</label>
                    <input
                        type="number"
                        min="0"
                        placeholder="Monto del abono"
                        value={montoAbono}
                        onChange={function(e) { setMontoAbono(e.target.value); }}
                    />
                </div>
                <div className="modal-campo">
                    <label className="modal-label">Fecha</label>
                    <input
                        type="date"
                        value={fechaAbono}
                        onChange={function(e) { setFechaAbono(e.target.value); }}
                    />
                </div>
                <div className="modal-campo">
                    <label className="modal-label">Notas — opcional</label>
                    <input
                        type="text"
                        placeholder="Observaciones del abono"
                        value={notasAbono}
                        onChange={function(e) { setNotasAbono(e.target.value); }}
                    />
                </div>

                <div className="modal-footer" style={{ justifyContent: 'space-between' }}>
                    {/* Boton marcar pagada */}
                    <button className="btn-secundario" onClick={function() { setMostrarPago(true); }}>
                        Marcar como pagada
                    </button>
                    <button className="btn-primario" onClick={handleRegistrarAbono}>
                        Registrar abono
                    </button>
                </div>

            </Modal>
        )}

        {/* Modal marcar como pagada desde abonos */}
        {mostrarPago && facturaSeleccionada !== null && mostrarAbonos === false && (
            <Modal
                titulo={'Confirmar pago — ' + facturaSeleccionada.numeroFactura}
                onCerrar={function() { setMostrarPago(false); }}>

                {error !== '' && (
                    <div className="error-mensaje">{error}</div>
                )}

                <div className="modal-campo">
                    <label className="modal-label">Descuento (%) — opcional</label>
                    <input
                        type="number"
                        min="0"
                        max="100"
                        placeholder="Ej: 10"
                        value={descuento}
                        onChange={function(e) { setDescuento(e.target.value); }}
                    />
                </div>

                <div className="modal-campo">
                    <label className="modal-label">% Comisión de pago (por defecto 4%)</label>
                    <input
                        type="number"
                        min="0"
                        max="100"
                        placeholder="4"
                        value={porcComisionPago}
                        onChange={function(e) { setPorcComisionPago(e.target.value); }}
                    />
                </div>

                <div className="modal-footer">
                    <button className="btn-secundario" onClick={function() { setMostrarPago(false); }}>
                        Cancelar
                    </button>
                    <button className="btn-primario" onClick={handleMarcarPagada}>
                        Confirmar pago
                    </button>
                </div>

            </Modal>
        )}

        {mostrarPrint && facturaImprimir && (
          <div
            style={{
              position: "fixed",
              inset: 0,
              background: "rgba(0,0,0,.45)",
              zIndex: 999999,
              overflow: "auto",
              padding: 24,
            }}
          >
            <div style={{ background: "#fff", maxWidth: 900, margin: "0 auto" }}>
              
              <div style={{ display: "flex", gap: 8, padding: 12, borderBottom: "1px solid #eee" }}>
                <button onClick={handleImprimirVentana}>Imprimir</button>
                <button onClick={() => setMostrarPrint(false)}>Cerrar</button>
              </div>

              {/* Aquí debería verse la factura */}
              <FacturaPrint factura={facturaImprimir} items={itemsPrint} />
            </div>
          </div>
        )}

              

              
    </div>

    
  );
}