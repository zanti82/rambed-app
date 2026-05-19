package Rambed360.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import Rambed360.entity.Talla;

public class FacturaDetalleResponse {

    // ID del detalle
    private Long id;

    // ID de la factura
    private Long facturaId;

    // Numero de factura
    private String numeroFactura;

    // ID del inventario
    private Long inventarioId;

    // Marca del producto
    private String marca;

    // Referencia del producto
    private String referencia;

    // Talla del producto
    private Talla talla;

    // Cantidad vendida
    private Integer cantidad;

    // Precio unitario al momento de la venta
    private BigDecimal precioUnitario;

    // Subtotal calculado cantidad por precio unitario
    private BigDecimal subtotal;

    // Fecha de creacion
    private LocalDateTime creadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public Long getInventarioId() { return inventarioId; }
    public void setInventarioId(Long inventarioId) { this.inventarioId = inventarioId; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Talla getTalla() { return talla; }
    public void setTalla(Talla talla) { this.talla = talla; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
