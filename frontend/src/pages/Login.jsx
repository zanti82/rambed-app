import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import authApi from '../api/authApi';
import '../styles/login.css';
import '../styles/global.css';


export default function Login() {

  // Estado para el campo username
  const [username, setUsername] = useState('');

  // Estado para el campo password
  const [password, setPassword] = useState('');

  // Estado para mostrar errores de login
  const [error, setError] = useState('');

  // Estado para deshabilitar el boton mientras carga
  const [cargando, setCargando] = useState(false);

  // Hook para redirigir despues del login
  const navigate = useNavigate();

  // Funcion login del contexto para guardar la sesion
  const { login } = useAuth();

  // Maneja el envio del formulario
  async function handleSubmit(e) {
    // Evita que el formulario recargue la pagina
    e.preventDefault();

    // Limpia el error anterior
    setError('');

    // Valida que los campos no esten vacios
    if (username.trim() === '' || password.trim() === '') {
      setError('El usuario y la contraseña son obligatorios');
      return;
    }

    // Activa el estado de carga
    setCargando(true);

    try {
      // Envia las credenciales al backend
      const respuesta = await authApi.login(username, password);

      // Guarda la sesion en el contexto y localStorage
      login(respuesta.data);

      // Redirige al dashboard
      navigate('/dashboard');

    } catch (err) {
      // Si las credenciales son incorrectas muestra el error
      if (err.response && err.response.status === 401) {
        setError('Usuario o contraseña incorrectos');
      } else {
        setError('Error al conectar con el servidor');
      }
    } finally {
      // Desactiva el estado de carga siempre
      setCargando(false);
    }
  }

  return (
    <div className="login-contenedor">
      <div className="login-card">

        {/* Logo de la app */}
        <div className="login-logo">
          <div className="login-logo-icono">R</div>
          <div>
            <div className="login-logo-nombre">Rambed360</div>
            <div className="login-logo-subtitulo">Sistema de gestión</div>
          </div>
        </div>

        {/* Titulo del formulario */}
        <h1 className="login-titulo">Bienvenido</h1>
        <p className="login-descripcion">Ingresa tus credenciales para continuar</p>

        {/* Mensaje de error */}
        {error !== '' && (
          <div className="login-error">{error}</div>
        )}

        {/* Formulario de login */}
        <form onSubmit={handleSubmit}>

          {/* Campo username */}
          <div className="login-campo">
            <label className="login-label">Usuario</label>
            <input
              type="text"
              placeholder="Ingresa tu usuario"
              value={username}
              onChange={function(e) { setUsername(e.target.value); }}
            />
          </div>

          {/* Campo password */}
          <div className="login-campo">
            <label className="login-label">Contraseña</label>
            <input
              type="password"
              placeholder="Ingresa tu contraseña"
              value={password}
              onChange={function(e) { setPassword(e.target.value); }}
            />
          </div>

          {/* Boton de submit */}
          <button
            type="submit"
            className="login-boton"
            disabled={cargando}>
            {cargando ? 'Ingresando...' : 'Ingresar'}
          </button>

        </form>
      </div>
    </div>
  );
}