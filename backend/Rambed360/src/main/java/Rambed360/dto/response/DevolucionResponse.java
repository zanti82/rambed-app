package Rambed360.dto.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import Rambed360.entity.Talla;

public class DevolucionResponse {

    // ID de la devolucion
    private Long id;

    // ID del item de factura devuelto
    private Long facturaDetalleId;

    // Numero de factura relacionada
    private String numeroFactura;

    // Marca del producto devuelto
    private String marca;

    // Referencia del producto devuelto
    private String referencia;

    // Talla del producto devuelto
    private Talla talla;

    // Cantidad devuelta
    private Integer cantidad;

    // Precio unitario del producto devuelto
    private BigDecimal precioUnitario;

    // Motivo de la devolucion
    private String motivo;

    // Fecha de la devolucion
    private LocalDateTime creadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFacturaDetalleId() { return facturaDetalleId; }
    public void setFacturaDetalleId(Long facturaDetalleId) { this.facturaDetalleId = facturaDetalleId; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

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

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}