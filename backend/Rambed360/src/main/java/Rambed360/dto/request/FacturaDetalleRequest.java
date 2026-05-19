package Rambed360.dto.request;

import java.math.BigDecimal;

public class FacturaDetalleRequest {

    // ID de la factura a la que pertenece este item
    private Long facturaId;

    // ID del registro de inventario
    private Long inventarioId;

    // Cantidad de unidades vendidas
    private Integer cantidad;

    // Precio unitario al momento de la venta
    private BigDecimal precioUnitario;

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public Long getInventarioId() { return inventarioId; }
    public void setInventarioId(Long inventarioId) { this.inventarioId = inventarioId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
