
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import RutaProtegida from './routes/RutaProtegida';
import RutaAdmin from './routes/RutaAdmin';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Vendedores from './pages/Vendedores';
import Referencias from './pages/Referencias';
import Inventario from './pages/Inventario';
import Clientes from './pages/Clientes';
import Facturas from './pages/Facturas';
import NuevaFactura from './pages/NuevaFactura';
import Devoluciones from './pages/Devoluciones';
import Usuarios from './pages/Usuarios';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Ruta publica */}
        <Route path="/login" element={<Login />} />

        {/* Rutas para cualquier usuario autenticado */}
        <Route path="/dashboard" element={<RutaProtegida><Dashboard /></RutaProtegida>} />
        <Route path="/inventario" element={<RutaProtegida><Inventario /></RutaProtegida>} />
        <Route path="/clientes" element={<RutaProtegida><Clientes /></RutaProtegida>} />
        <Route path="/facturas" element={<RutaProtegida><Facturas /></RutaProtegida>} />
        <Route path="/facturas/nueva" element={<RutaProtegida><NuevaFactura /></RutaProtegida>} />
        <Route path="/devoluciones" element={<RutaProtegida><Devoluciones /></RutaProtegida>} />

        {/* Rutas solo para ADMIN */}
        <Route path="/vendedores" element={<RutaAdmin><Vendedores /></RutaAdmin>} />
        <Route path="/referencias" element={<RutaAdmin><Referencias /></RutaAdmin>} />
        <Route path="/usuarios" element={<RutaAdmin><Usuarios /></RutaAdmin>} />

        {/* Redirige la raiz al dashboard */}
        <Route path="/" element={<Navigate to="/dashboard" />} />

      </Routes>
    </BrowserRouter>
  );
}