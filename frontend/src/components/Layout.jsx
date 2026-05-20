import Sidebar from './Sidebar';

// Envuelve cada pagina con el sidebar y el contenido principal
export default function Layout({ children }) {
  return (
    <div className="layout">

      {/* Sidebar fijo a la izquierda */}
      <Sidebar />

      {/* Contenido de la pagina al lado del sidebar */}
      <main className="contenido-principal">
        {children}
      </main>

    </div>
  );
}