import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';

export default function RutaProtegida({ children }) {
  // Obtiene el usuario del contexto
  const { usuario } = useAuth();

  // Si no hay usuario redirige al login
  if (usuario === null) {
    return <Navigate to="/login" />;
  }

  // Si hay usuario muestra el contenido dentro del layout
  return <Layout>{children}</Layout>;
}