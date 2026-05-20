import { createContext, useState, useContext } from 'react';

// Crea el contexto de autenticacion
const AuthContext = createContext(null);

// Proveedor que envuelve toda la app y da acceso al estado de autenticacion
export function AuthProvider({ children }) {

  // Inicializa el usuario desde localStorage si ya habia una sesion
  const [usuario, setUsuario] = useState(function() {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      return JSON.parse(usuarioGuardado);
    }
    return null;
  });

  // Guarda el usuario y el token despues de un login exitoso
  function login(datos) {
    // Guarda el token JWT en localStorage
    localStorage.setItem('token', datos.token);

    // Guarda los datos del usuario en localStorage
    localStorage.setItem('usuario', JSON.stringify({
      username: datos.username,
      rol: datos.rol,
      vendedorId: datos.vendedorId
    }));

    // Actualiza el estado del usuario
    setUsuario({
      username: datos.username,
      rol: datos.rol,
      vendedorId: datos.vendedorId
    });
  }

  // Limpia la sesion al hacer logout
  function logout() {
    // Elimina el token del localStorage
    localStorage.removeItem('token');

    // Elimina los datos del usuario del localStorage
    localStorage.removeItem('usuario');

    // Limpia el estado del usuario
    setUsuario(null);
  }

  // Verifica si el usuario tiene rol ADMIN
  function esAdmin() {
    if (usuario && usuario.rol === 'ADMIN') {
      return true;
    }
    return false;
  }

  // Verifica si el usuario tiene rol VENDEDOR
  function esVendedor() {
    if (usuario && usuario.rol === 'VENDEDOR') {
      return true;
    }
    return false;
  }

  // Valor que se comparte con toda la app
  const valor = {
    usuario,
    login,
    logout,
    esAdmin,
    esVendedor
  };

  return (
    <AuthContext.Provider value={valor}>
      {children}
    </AuthContext.Provider>
  );
}

// Hook personalizado para usar el contexto facilmente
export function useAuth() {
  return useContext(AuthContext);
}