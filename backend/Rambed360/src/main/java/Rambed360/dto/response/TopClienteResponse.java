package Rambed360.dto.response;


import java.math.BigDecimal;

// Datos de un cliente en el top 20
public class TopClienteResponse {

    // ID del cliente
    private Long clienteId;

    // Nombre del cliente
    private String clienteNombre;

    // Nombre del almacen
    private String clienteAlmacen;

    // Total facturado por este cliente
    private BigDecimal totalFacturado;

    public TopClienteResponse() {}

    public TopClienteResponse(Long clienteId, String clienteNombre, String clienteAlmacen, BigDecimal totalFacturado) {
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.clienteAlmacen = clienteAlmacen;
        this.totalFacturado = totalFacturado;
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteAlmacen() { return clienteAlmacen; }
    public void setClienteAlmacen(String clienteAlmacen) { this.clienteAlmacen = clienteAlmacen; }

    public BigDecimal getTotalFacturado() { return totalFacturado; }
    public void setTotalFacturado(BigDecimal totalFacturado) { this.totalFacturado = totalFacturado; }
}
