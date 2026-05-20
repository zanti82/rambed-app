import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Vendedores from './pages/Vendedores';
import Referencias from './pages/Referencias';
import Inventario from './pages/Inventario';
import Clientes from './pages/Clientes';
import Facturas from './pages/Facturas';
import NuevaFactura from './pages/NuevaFactura';
import Devoluciones from './pages/Devoluciones';

// Componente que protege rutas que requieren autenticacion
function RutaProtegida({ children }) {
  // Obtiene el usuario del contexto de autenticacion
  const { usuario } = useAuth();

  // Si no hay usuario redirige al login
  if (usuario === null) {
    return <Navigate to="/login" />;
  }

  // Si hay usuario muestra el contenido dentro del layout
  return <Layout>{children}</Layout>;
}

// Componente que protege rutas que requieren rol ADMIN
function RutaAdmin({ children }) {
  // Obtiene el usuario y la funcion esAdmin del contexto
  const { usuario, esAdmin } = useAuth();

  // Si no hay usuario redirige al login
  if (usuario === null) {
    return <Navigate to="/login" />;
  }

  // Si no es admin redirige al dashboard
  if (esAdmin() === false) {
    return <Navigate to="/dashboard" />;
  }

  // Si es admin muestra el contenido dentro del layout
  return <Layout>{children}</Layout>;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Ruta publica de login */}
        <Route path="/login" element={<Login />} />

        {/* Rutas protegidas que requieren autenticacion */}
        <Route path="/dashboard" element={
          <RutaProtegida><Dashboard /></RutaProtegida>
        } />

        <Route path="/inventario" element={
          <RutaProtegida><Inventario /></RutaProtegida>
        } />

        <Route path="/clientes" element={
          <RutaProtegida><Clientes /></RutaProtegida>
        } />

        <Route path="/facturas" element={
          <RutaProtegida><Facturas /></RutaProtegida>
        } />

        <Route path="/facturas/nueva" element={
          <RutaProtegida><NuevaFactura /></RutaProtegida>
        } />

        <Route path="/devoluciones" element={
          <RutaProtegida><Devoluciones /></RutaProtegida>
        } />

        {/* Rutas solo para ADMIN */}
        <Route path="/vendedores" element={
          <RutaAdmin><Vendedores /></RutaAdmin>
        } />

        <Route path="/referencias" element={
          <RutaAdmin><Referencias /></RutaAdmin>
        } />

        {/* Redirige la raiz al dashboard */}
        <Route path="/" element={<Navigate to="/dashboard" />} />
      </Routes>
    </BrowserRouter>
  );
}