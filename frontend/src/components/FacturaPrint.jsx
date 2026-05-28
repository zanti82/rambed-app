import logoRambed from '../assets/logo.png';

export default function FacturaPrint({ factura, items }) {

  // Formatea un numero como moneda colombiana
  function formatearMoneda(valor) {
    return '$' + Number(valor).toLocaleString('es-CO');
  }

  // Agrupa los items por referencia sumando cantidades y subtotales
function agruparPorReferencia(items) {
    // Objeto para acumular los grupos
    const grupos = {};
  
    // Recorre cada item y lo agrupa por referencia
    for (let i = 0; i < items.length; i++) {
      const item = items[i];
  
      // Crea la clave unica por marca y referencia
      const clave = item.marca + '-' + item.referencia;
  
      // Si el grupo no existe lo crea
      if (grupos[clave] === undefined) {
        grupos[clave] = {
          marca: item.marca,
          referencia: item.referencia,
          cantidad: 0,
          subtotal: 0,
          tallas: [],
          precioUnitario: Number(item.precioUnitario)
        };
      }
  
      // Suma la cantidad al grupo
      grupos[clave].cantidad = grupos[clave].cantidad + item.cantidad;
  
      // Suma el subtotal al grupo
      grupos[clave].subtotal = grupos[clave].subtotal + (Number(item.precioUnitario) * Number(item.cantidad));
  
      // Agrega la talla con su cantidad
      grupos[clave].tallas.push(item.talla + ' x' + item.cantidad);
    }
  
    // Convierte el objeto a arreglo para poder recorrerlo
    const resultado = [];
    const claves = Object.keys(grupos);
    for (let i = 0; i < claves.length; i++) {
      resultado.push(grupos[claves[i]]);
    }
  
    return resultado;
  }

  return (
    <div id="factura-print" style={{
      fontFamily: 'Arial, sans-serif',
      color: '#000',
      background: '#fff',
      padding: '40px',
      maxWidth: '800px',
      margin: '0 auto'
    }}>

      {/* Cabecera con logo y datos de la empresa */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 32, borderBottom: '2px solid #e63946', paddingBottom: 20 }}>

        {/* Logo y nombre */}
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <img
            src={logoRambed}
            alt="Rambed360"
            style={{ width: 80, height: 'auto' }}
          />
          <div>
            <div style={{ fontSize: 22, fontWeight: 700, color: '#e63946' }}>Rambed360</div>
            <div style={{ fontSize: 13, color: '#666' }}>Sistema de gestión empresarial</div>
          </div>
        </div>

        {/* Numero y estado de la factura */}
        <div style={{ textAlign: 'right' }}>
          <div style={{ fontSize: 24, fontWeight: 700, color: '#000' }}>{factura.numeroFactura}</div>
          <div style={{
            display: 'inline-block',
            padding: '4px 12px',
            borderRadius: 20,
            fontSize: 12,
            fontWeight: 600,
            marginTop: 6,
            background: factura.estado === 'pagada' ? '#d4edda' : factura.estado === 'anulada' ? '#f8d7da' : '#fff3cd',
            color: factura.estado === 'pagada' ? '#155724' : factura.estado === 'anulada' ? '#721c24' : '#856404'
          }}>
            {factura.estado.toUpperCase()}
          </div>
        </div>
      </div>

      {/* Informacion de cliente y vendedor */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24, marginBottom: 32 }}>

        {/* Datos del cliente */}
        <div style={{ background: '#f8f8f8', borderRadius: 8, padding: 16, border: '1px solid #000000' }}>
          <div style={{ fontSize: 11, fontWeight: 600, color: '#999', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 10 }}>
            Cliente
          </div>
          <div style={{ fontSize: 15, fontWeight: 600, marginBottom: 4 }}>{factura.clienteNombre}</div>
          <div style={{ fontSize: 13,fontWeight: 600 }}>{factura.clienteDireccion}</div>
          <div style={{ fontSize: 13,fontWeight: 600 }}>{factura.clienteTelefono}</div>
        </div>

        {/* Datos de la factura */}
        <div style={{ background: '#f8f8f8', borderRadius: 8, padding: 16, border: '1px solid #000000' }}>
          <div style={{ fontSize: 11, fontWeight: 600, color: '#999', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 10 }}>
            Detalles
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6 }}>
            <span style={{ fontSize: 13, color: '#555' }}>Vendedor</span>
            <span style={{ fontSize: 13, fontWeight: 500 }}>{factura.vendedorNombre}</span>
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6 }}>
            <span style={{ fontSize: 13, color: '#555' }}>Fecha emisión</span>
            <span style={{ fontSize: 13, fontWeight: 500 }}>{factura.fechaEmision}</span>
          </div>
          {factura.fechaPago && (
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 13, color: '#555' }}>Fecha pago</span>
              <span style={{ fontSize: 13, fontWeight: 500 }}>{factura.fechaPago}</span>
            </div>
          )}
        </div>
      </div>

      {/* Tabla de productos */}
      <table style={{ width: '100%', borderCollapse: 'collapse', marginBottom: 24 }}>
        <thead>
        <tr style={{ background: '#e63946' }}>
            <th style={{ padding: '10px 14px', textAlign: 'left', fontSize: 12, fontWeight: 600, color: '#fff', textTransform: 'uppercase' }}>Producto</th>
            <th style={{ padding: '10px 14px', textAlign: 'center', fontSize: 12, fontWeight: 600, color: '#fff', textTransform: 'uppercase' }}>Cantidad</th>
            <th style={{ padding: '10px 14px', textAlign: 'right', fontSize: 12, fontWeight: 600, color: '#fff', textTransform: 'uppercase' }}>Precio unit.</th>
            <th style={{ padding: '10px 14px', textAlign: 'right', fontSize: 12, fontWeight: 600, color: '#fff', textTransform: 'uppercase' }}>Subtotal</th>
        </tr>
        </thead>
        <tbody>
        {agruparPorReferencia(items).map(function(grupo, index) {
            return (
                <tr key={index} style={{ background: index % 2 === 0 ? '#fff' : '#f8f8f8' }}>
                <td style={{ padding: '10px 14px', borderBottom: '1px solid #eee' }}>
                    <div style={{ fontWeight: 500, fontSize: 14 }}>{grupo.marca} — {grupo.referencia}</div>
                    <div style={{ fontSize: 12, color: '#888' }}>{grupo.tallas.join(', ')}</div>
                </td>
                <td style={{ padding: '10px 14px', textAlign: 'center', fontSize: 14, borderBottom: '1px solid #eee' }}>
                    {grupo.cantidad}
                </td>
                <td style={{ padding: '10px 14px', textAlign: 'right', fontSize: 14, borderBottom: '1px solid #eee' }}>
                {formatearMoneda(grupo.precioUnitario)}
                </td>
                <td style={{ padding: '10px 14px', textAlign: 'right', fontSize: 14, fontWeight: 500, borderBottom: '1px solid #eee' }}>
                    {formatearMoneda(grupo.subtotal)}
                </td>
                </tr>
            );
            })}
        </tbody>
      </table>

      {/* Totales */}
      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 32, border: '1px solid #000000' }}>
        <div style={{ minWidth: 260 }}>

          {/* Subtotal */}
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #eee' }}>
            <span style={{ fontSize: 14, color: '#555' }}>Subtotal</span>
            <span style={{ fontSize: 14 }}>{formatearMoneda(factura.subtotal)}</span>
          </div>

          {/* Descuento si aplica */}
          {Number(factura.descuentoPorcentaje) > 0 && (
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #eee' }}>
              <span style={{ fontSize: 14, color: '#555' }}>Descuento ({factura.descuentoPorcentaje}%)</span>
              <span style={{ fontSize: 14, color: '#e63946' }}>
                -{formatearMoneda(factura.subtotal - factura.total)}
              </span>
            </div>
          )}

          {/* Total */}
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '12px 0' }}>
            <span style={{ fontSize: 16, fontWeight: 700 }}>Total</span>
            <span style={{ fontSize: 16, fontWeight: 700, color: '#e63946' }}>
              {formatearMoneda(factura.total)}
            </span>
          </div>
        </div>
      </div>

      {/* Notas si existen */}
      {factura.notas && (
        <div style={{ padding: 16, background: '#f8f8f8', borderRadius: 8, marginBottom: 24, border: '1px solid #e0e0e0' }}>
          <div style={{ fontSize: 12, fontWeight: 600, color: '#999', textTransform: 'uppercase', marginBottom: 6 }}>Notas</div>
          <div style={{ fontSize: 13, color: '#555' }}>{factura.notas}</div>
        </div>
      )}

      {/* Pie de pagina */}
      <div style={{ borderTop: '2px solid #e63946', paddingTop: 16, textAlign: 'center' }}>
        <div style={{ fontSize: 12, color: '#999' }}>Rambed360 — Sistema de gestión empresarial</div>
        <div style={{ fontSize: 12, color: '#999' }}>Cuenta ahorros bancolombia 31356626659</div>
        <div style={{ fontSize: 11, color: '#bbb', marginTop: 4 }}>Documento generado el {new Date().toLocaleDateString('es-CO')}</div>
      </div>

    </div>
  );
}