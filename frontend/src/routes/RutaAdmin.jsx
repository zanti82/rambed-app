import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';

export default function RutaAdmin({ children }) {
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