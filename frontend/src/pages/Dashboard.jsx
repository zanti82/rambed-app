import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import facturasApi from '../api/facturasApi';
import clientesApi from '../api/clientesApi';
import inventarioApi from '../api/inventarioApi';
import '../styles/dashboard.css';

export default function Dashboard() {

  // Obtiene el usuario del contexto
  const { usuario, esAdmin } = useAuth();

  // Hook para navegar a otras paginas
  const navigate = useNavigate();

  // Estado para las facturas recientes
  const [facturasRecientes, setFacturasRecientes] = useState([]);

  // Estado para las facturas pendientes
  const [facturasPendientes, setFacturasPendientes] = useState([]);

  // Estado para el total de clientes
  const [totalClientes, setTotalClientes] = useState(0);

  // Estado para el total de productos con stock
  const [totalProductos, setTotalProductos] = useState(0);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Carga los datos al montar el componente
  useEffect(function() {
    cargarDatos();
  }, []);

  // Carga todos los datos del dashboard
  async function cargarDatos() {
    setCargando(true);
    try {
      // Si es admin carga todas las facturas
      // Si es vendedor carga solo las suyas
      let respuestaFacturas = null;

      if (esAdmin()) {
        respuestaFacturas = await facturasApi.listarTodas();
      } else {
        respuestaFacturas = await facturasApi.listarPorVendedor(usuario.vendedorId);
      }

      // Guarda todas las facturas
      const todasLasFacturas = respuestaFacturas.data;

      // Filtra las ultimas 5 facturas para mostrar en recientes
      const ultimas = todasLasFacturas.slice(-5).reverse();
      setFacturasRecientes(ultimas);

      // Filtra las facturas pendientes
      const pendientes = todasLasFacturas.filter(function(factura) {
        return factura.estado === 'pendiente';
      });
      setFacturasPendientes(pendientes);

      // Carga el total de clientes
      const respuestaClientes = await clientesApi.listarActivos();
      setTotalClientes(respuestaClientes.data.length);

      // Carga el total de productos con stock
      const respuestaInventario = await inventarioApi.listarConStock();
      setTotalProductos(respuestaInventario.data.length);

    } catch (err) {
      console.error('Error al cargar el dashboard:', err);
    } finally {
      setCargando(false);
    }
  }

  // Calcula el total vendido de una lista de facturas
  function calcularTotalVendido(facturas) {
    let total = 0;
    for (let i = 0; i < facturas.length; i++) {
      total = total + facturas[i].total;
    }
    return total;
  }

  // Formatea un numero como moneda colombiana
  function formatearMoneda(valor) {
    return '$' + Number(valor).toLocaleString('es-CO');
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

  return (
    <div>
      {/* Titulo de la pagina */}
      <h1 className="titulo-pagina">
        Bienvenido, {usuario ? usuario.username : ''}
      </h1>

      {/* Grid de tarjetas de resumen */}
      <div className="dashboard-grid">

        {/* Card de facturas pendientes */}
        <div className="dashboard-card">
          <div className="dashboard-card-header">
            <span className="dashboard-card-titulo">Facturas pendientes</span>
            <span className="dashboard-card-icono">🧾</span>
          </div>
          <div className="dashboard-card-valor">
            {cargando ? '...' : facturasPendientes.length}
          </div>
          <div className="dashboard-card-descripcion">Por cobrar</div>
          <div className="dashboard-card-linea" style={{ backgroundColor: '#e63946' }}></div>
        </div>

        {/* Card de total por cobrar */}
        <div className="dashboard-card">
          <div className="dashboard-card-header">
            <span className="dashboard-card-titulo">Total por cobrar</span>
            <span className="dashboard-card-icono">💰</span>
          </div>
          <div className="dashboard-card-valor">
            {cargando ? '...' : formatearMoneda(calcularTotalVendido(facturasPendientes))}
          </div>
          <div className="dashboard-card-descripcion">Facturas pendientes</div>
          <div className="dashboard-card-linea" style={{ backgroundColor: '#ffc107' }}></div>
        </div>

        {/* Card de clientes activos */}
        <div className="dashboard-card">
          <div className="dashboard-card-header">
            <span className="dashboard-card-titulo">Clientes activos</span>
            <span className="dashboard-card-icono">👥</span>
          </div>
          <div className="dashboard-card-valor">
            {cargando ? '...' : totalClientes}
          </div>
          <div className="dashboard-card-descripcion">Registrados en el sistema</div>
          <div className="dashboard-card-linea" style={{ backgroundColor: '#4caf82' }}></div>
        </div>

        {/* Card de productos en stock */}
        <div className="dashboard-card">
          <div className="dashboard-card-header">
            <span className="dashboard-card-titulo">Productos en stock</span>
            <span className="dashboard-card-icono">📦</span>
          </div>
          <div className="dashboard-card-valor">
            {cargando ? '...' : totalProductos}
          </div>
          <div className="dashboard-card-descripcion">Referencias disponibles</div>
          <div className="dashboard-card-linea" style={{ backgroundColor: '#a0a0a0' }}></div>
        </div>

      </div>

      {/* Seccion de actividad reciente */}
      <div className="dashboard-seccion">

        {/* Facturas recientes */}
        <div className="dashboard-actividad">
          <div className="dashboard-actividad-titulo">Facturas recientes</div>

          {/* Estado de carga */}
          {cargando && (
            <div className="dashboard-cargando">Cargando...</div>
          )}

          {/* Sin facturas */}
          {cargando === false && facturasRecientes.length === 0 && (
            <div className="dashboard-vacio">No hay facturas registradas</div>
          )}

          {/* Lista de facturas recientes */}
          {cargando === false && facturasRecientes.map(function(factura) {
            return (
              <div key={factura.id} className="dashboard-actividad-fila">
                <div>
                  <div className="dashboard-actividad-texto">{factura.numeroFactura}</div>
                  <div className="dashboard-actividad-secundario">{factura.clienteNombre}</div>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 4 }}>
                  <div className="dashboard-actividad-valor">{formatearMoneda(factura.total)}</div>
                  <span className={obtenerClaseBadge(factura.estado)}>{factura.estado}</span>
                </div>
              </div>
            );
          })}

          {/* Boton para ver todas las facturas */}
          {cargando === false && facturasRecientes.length > 0 && (
            <button
              className="btn-secundario"
              style={{ width: '100%', marginTop: 16 }}
              onClick={function() { navigate('/facturas'); }}>
              Ver todas las facturas
            </button>
          )}
        </div>

        {/* Facturas pendientes */}
        <div className="dashboard-actividad">
          <div className="dashboard-actividad-titulo">Pendientes de cobro</div>

          {/* Estado de carga */}
          {cargando && (
            <div className="dashboard-cargando">Cargando...</div>
          )}

          {/* Sin pendientes */}
          {cargando === false && facturasPendientes.length === 0 && (
            <div className="dashboard-vacio">No hay facturas pendientes</div>
          )}

          {/* Lista de facturas pendientes */}
          {cargando === false && facturasPendientes.slice(0, 5).map(function(factura) {
            return (
              <div key={factura.id} className="dashboard-actividad-fila">
                <div>
                  <div className="dashboard-actividad-texto">{factura.numeroFactura}</div>
                  <div className="dashboard-actividad-secundario">{factura.clienteAlmacen}</div>
                </div>
                <div className="dashboard-actividad-valor">
                  {formatearMoneda(factura.subtotal)}
                </div>
              </div>
            );
          })}

          {/* Boton para crear nueva factura */}
          <button
            className="btn-primario"
            style={{ width: '100%', marginTop: 16 }}
            onClick={function() { navigate('/facturas/nueva'); }}>
            + Nueva factura
          </button>
        </div>

      </div>
    </div>
  );
}