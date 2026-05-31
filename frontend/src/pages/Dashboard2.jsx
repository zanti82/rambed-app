import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import dashboardApi from '../api/dashboardApi';
import '../styles/dashboard.css';

export default function Dashboard() {

  // Obtiene el usuario del contexto
  const { usuario, esAdmin } = useAuth();

  // Hook para navegar a otras paginas
  const navigate = useNavigate();

  // Estado para los datos del dashboard
  const [datos, setDatos] = useState(null);

  // Estado de carga
  const [cargando, setCargando] = useState(true);

  // Carga los datos al montar el componente
  useEffect(function() {
    cargarDatos();
  }, []);

  // Obtiene los datos del dashboard del backend
  async function cargarDatos() {
    setCargando(true);
    try {
      const respuesta = await dashboardApi.obtenerDashboard();
      setDatos(respuesta.data);
    } catch (err) {
      console.error('Error al cargar el dashboard:', err);
    } finally {
      setCargando(false);
    }
  }

  // Formatea un numero como moneda colombiana
  function formatearMoneda(valor) {
    if (valor === null || valor === undefined) {
      return '—';
    }
    return '$' + Number(valor).toLocaleString('es-CO');
  }

  // Muestra cargando mientras se obtienen los datos
  if (cargando) {
    return <div style={{ padding: 32, color: 'var(--gris-texto)' }}>Cargando...</div>;
  }

  // Si no hay datos muestra mensaje
  if (datos === null) {
    return <div style={{ padding: 32, color: 'var(--gris-texto)' }}>No se pudieron cargar los datos</div>;
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
          <div className="dashboard-card-valor">{datos.totalPendientes}</div>
          <div className="dashboard-card-descripcion">Por cobrar</div>
          <div className="dashboard-card-linea" style={{ backgroundColor: '#e63946' }}></div>
        </div>

        {/* Card de total por cobrar */}
        <div className="dashboard-card">
          <div className="dashboard-card-header">
            <span className="dashboard-card-titulo">Total por cobrar</span>
            <span className="dashboard-card-icono">💰</span>
          </div>
          <div className="dashboard-card-valor">{formatearMoneda(datos.totalPorCobrar)}</div>
          <div className="dashboard-card-descripcion">Facturas pendientes</div>
          <div className="dashboard-card-linea" style={{ backgroundColor: '#ffc107' }}></div>
        </div>

        {/* Card de clientes activos - solo admin */}
        {esAdmin() && (
          <div className="dashboard-card">
            <div className="dashboard-card-header">
              <span className="dashboard-card-titulo">Clientes activos</span>
              <span className="dashboard-card-icono">👥</span>
            </div>
            <div className="dashboard-card-valor">{datos.totalClientes}</div>
            <div className="dashboard-card-descripcion">Registrados en el sistema</div>
            <div className="dashboard-card-linea" style={{ backgroundColor: '#4caf82' }}></div>
          </div>
        )}

        {/* Card de productos en stock - solo admin */}
        {esAdmin() && (
          <div className="dashboard-card">
            <div className="dashboard-card-header">
              <span className="dashboard-card-titulo">Productos en stock</span>
              <span className="dashboard-card-icono">📦</span>
            </div>
            <div className="dashboard-card-valor">{datos.totalProductos}</div>
            <div className="dashboard-card-descripcion">Referencias disponibles</div>
            <div className="dashboard-card-linea" style={{ backgroundColor: '#a0a0a0' }}></div>
          </div>
        )}

        {/* Card de costo total inventario - solo admin */}
        {esAdmin() && (
          <div className="dashboard-card">
            <div className="dashboard-card-header">
              <span className="dashboard-card-titulo">Costo inventario</span>
              <span className="dashboard-card-icono">🏷️</span>
            </div>
            <div className="dashboard-card-valor">{formatearMoneda(datos.costoTotalInventario)}</div>
            <div className="dashboard-card-descripcion">Valor total en bodega</div>
            <div className="dashboard-card-linea" style={{ backgroundColor: '#e63946' }}></div>
          </div>
        )}

        {/* Card de utilidad total - solo admin */}
        {esAdmin() && (
          <div className="dashboard-card">
            <div className="dashboard-card-header">
              <span className="dashboard-card-titulo">Utilidad total</span>
              <span className="dashboard-card-icono">📈</span>
            </div>
            <div className="dashboard-card-valor">{formatearMoneda(datos.utilidadTotal)}</div>
            <div className="dashboard-card-descripcion">Facturas pagadas</div>
            <div className="dashboard-card-linea" style={{ backgroundColor: '#4caf82' }}></div>
          </div>
        )}

      </div>

      {/* Seccion de top 10 - solo admin */}
      {esAdmin() && datos.top20Clientes !== null && (
        <div className="dashboard-seccion">

          {/* Top 10 clientes */}
          <div className="dashboard-actividad">
            <div className="dashboard-actividad-titulo">Top 20 clientes</div>

            {/* Sin datos */}
            {datos.top20Clientes.length === 0 && (
              <div className="dashboard-vacio">Sin datos aun</div>
            )}

            {/* Lista de clientes */}
            {datos.top20Clientes.map(function(cliente, index) {
              return (
                <div key={cliente.clienteId} className="dashboard-actividad-fila">
                  <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                    {/* Numero de posicion */}
                    <div style={{
                      width: 24,
                      height: 24,
                      borderRadius: '50%',
                      background: index === 0 ? '#ffc107' : index === 1 ? '#a0a0a0' : index === 2 ? '#cd7f32' : 'var(--fondo-terciario)',
                      color: index < 3 ? '#000' : 'var(--gris-texto)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: 11,
                      fontWeight: 700,
                      flexShrink: 0
                    }}>
                      {index + 1}
                    </div>
                    <div>
                      <div className="dashboard-actividad-texto">{cliente.clienteNombre}</div>
                      <div className="dashboard-actividad-secundario">{cliente.clienteAlmacen}</div>
                    </div>
                  </div>
                  <div className="dashboard-actividad-valor">
                    {formatearMoneda(cliente.totalFacturado)}
                  </div>
                </div>
              );
            })}
          </div>

          {/* Top 10 referencias */}
          <div className="dashboard-actividad">
            <div className="dashboard-actividad-titulo">Top 20 referencias más vendidas</div>

            {/* Sin datos */}
            {datos.top20Referencias.length === 0 && (
              <div className="dashboard-vacio">Sin datos aun</div>
            )}

            {/* Lista de referencias */}
            {datos.top20Referencias.map(function(ref, index) {
              return (
                <div key={index} className="dashboard-actividad-fila">
                  <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                    {/* Numero de posicion */}
                    <div style={{
                      width: 24,
                      height: 24,
                      borderRadius: '50%',
                      background: index === 0 ? '#ffc107' : index === 1 ? '#a0a0a0' : index === 2 ? '#cd7f32' : 'var(--fondo-terciario)',
                      color: index < 3 ? '#000' : 'var(--gris-texto)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: 11,
                      fontWeight: 700,
                      flexShrink: 0
                    }}>
                      {index + 1}
                    </div>
                    <div>
                      <div className="dashboard-actividad-texto">{ref.marca} — {ref.referencia}</div>
                      <div className="dashboard-actividad-secundario">{ref.totalUnidades} unidades vendidas</div>
                    </div>
                  </div>
                </div>
              );
            })}

            {/* Boton nueva factura */}
            <button
              className="btn-primario"
              style={{ width: '100%', marginTop: 16 }}
              onClick={function() { navigate('/facturas/nueva'); }}>
              + Nueva factura
            </button>
          </div>

        </div>
      )}

      {/* Vista del vendedor - solo facturas pendientes */}
      {esAdmin() === false && (
        <div className="dashboard-actividad" style={{ marginTop: 16 }}>
          <div className="dashboard-actividad-titulo">Pendientes de cobro</div>

          <div className="dashboard-vacio" style={{ paddingTop: 8 }}>
            Tienes {datos.totalPendientes} factura(s) pendiente(s) por un total de {formatearMoneda(datos.totalPorCobrar)}
          </div>

          <button
            className="btn-primario"
            style={{ width: '100%', marginTop: 16 }}
            onClick={function() { navigate('/facturas/nueva'); }}>
            + Nueva factura
          </button>
        </div>
      )}

    </div>
  );
}