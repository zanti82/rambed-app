import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/navbar.css';

export default function Sidebar() {

  // Obtiene el usuario, logout y esAdmin del contexto
  const { usuario, logout, esAdmin } = useAuth();

  // Hook para redirigir despues del logout
  const navigate = useNavigate();

  // Maneja el cierre de sesion
  function handleLogout() {
    // Limpia la sesion del contexto y localStorage
    logout();

    // Redirige al login
    navigate('/login');
  }

  // Obtiene las dos primeras letras del username para el avatar
  function obtenerIniciales() {
    if (usuario && usuario.username) {
      return usuario.username.slice(0, 2).toUpperCase();
    }
    return 'US';
  }

  return (
    <aside className="sidebar">

      {/* Logo y nombre de la app */}
      <div className="sidebar-logo">
        <div className="sidebar-logo-icono">R</div>
        <div>
          <div className="sidebar-logo-nombre">Rambed360</div>
          <div className="sidebar-logo-subtitulo">Panel de gestión</div>
        </div>
      </div>
      
      {/* Menu de navegacion */}
      <nav className="sidebar-nav">

        {/* Seccion principal */}
        <span className="sidebar-seccion">Principal</span>

        <NavLink
          to="/dashboard"
          className={function({ isActive }) {
            return isActive ? 'sidebar-item activo' : 'sidebar-item';
          }}>
          <span className="sidebar-item-icono">📊</span>
          Dashboard
        </NavLink>

        {/* Seccion de ventas */}
        <span className="sidebar-seccion">Ventas</span>

        <NavLink
          to="/facturas"
          className={function({ isActive }) {
            return isActive ? 'sidebar-item activo' : 'sidebar-item';
          }}>
          <span className="sidebar-item-icono">🧾</span>
          Facturas
        </NavLink>

        <NavLink
          to="/clientes"
          className={function({ isActive }) {
            return isActive ? 'sidebar-item activo' : 'sidebar-item';
          }}>
          <span className="sidebar-item-icono">👥</span>
          Clientes
        </NavLink>

        <NavLink
          to="/devoluciones"
          className={function({ isActive }) {
            return isActive ? 'sidebar-item activo' : 'sidebar-item';
          }}>
          <span className="sidebar-item-icono">↩️</span>
          Devoluciones
        </NavLink>

        {/* Seccion de inventario */}
        <span className="sidebar-seccion">Inventario</span>

        <NavLink
          to="/inventario"
          className={function({ isActive }) {
            return isActive ? 'sidebar-item activo' : 'sidebar-item';
          }}>
          <span className="sidebar-item-icono">📦</span>
          Inventario
        </NavLink>

        {/* Seccion de administracion solo para ADMIN */}
        {esAdmin() && (
          <span className="sidebar-seccion">Administración</span>
        )}

        {esAdmin() && (
          <NavLink
            to="/vendedores"
            className={function({ isActive }) {
              return isActive ? 'sidebar-item activo' : 'sidebar-item';
            }}>
            <span className="sidebar-item-icono">🧑‍💼</span>
            Vendedores
          </NavLink>
        )}

        {esAdmin() && (
          <span className="sidebar-seccion">Administración</span>
        )}

        {esAdmin() && (
          <NavLink
            to="/dashboard2"
            className={function({ isActive }) {
              return isActive ? 'sidebar-item activo' : 'sidebar-item';
            }}>
            <span className="sidebar-item-icono">🧑‍💼</span>
            Dashboard info
          </NavLink>
        )}

        {esAdmin() && (
          <NavLink
            to="/referencias"
            className={function({ isActive }) {
              return isActive ? 'sidebar-item activo' : 'sidebar-item';
            }}>
            <span className="sidebar-item-icono">🏷️</span>
            Referencias
          </NavLink>
        )}

        {esAdmin() && (
          <NavLink
            to="/usuarios"
            className={function({ isActive }) {
              return isActive ? 'sidebar-item activo' : 'sidebar-item';
            }}>
            <span className="sidebar-item-icono">🔐</span>
            Usuarios
          </NavLink>
        )}

      </nav>

      {/* Footer con info del usuario y logout */}
      <div className="sidebar-footer">

        {/* Info del usuario actual */}
        <div className="sidebar-usuario">
          <div className="sidebar-usuario-avatar">{obtenerIniciales()}</div>
          <div>
            <div className="sidebar-usuario-nombre">{usuario ? usuario.username : ''}</div>
            <div className="sidebar-usuario-rol">{usuario ? usuario.rol : ''}</div>
          </div>
        </div>
        {/* Boton de cerrar sesion */}
      <button className="sidebar-logout" onClick={handleLogout}>
          <span>🚪</span>
          Cerrar sesión
        </button>

        
      </div>
    </aside>
  );
}