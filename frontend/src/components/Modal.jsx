import '../styles/modal.css';

export default function Modal({ titulo, onCerrar, children }) {

  // Cierra el modal al hacer clic en el overlay
  function handleOverlayClick(e) {
    // Solo cierra si el clic fue en el overlay, no en el contenido
    if (e.target === e.currentTarget) {
      onCerrar();
    }
  }

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-contenedor">

        {/* Cabecera con titulo y boton de cerrar */}
        <div className="modal-header">
          <h2 className="modal-titulo">{titulo}</h2>
          <button className="modal-cerrar" onClick={onCerrar}>✕</button>
        </div>

        {/* Contenido del modal */}
        <div className="modal-cuerpo">
          {children}
        </div>

      </div>
    </div>
  );
}