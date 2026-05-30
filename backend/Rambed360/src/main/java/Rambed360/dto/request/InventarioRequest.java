package Rambed360.dto.request;


import java.math.BigDecimal;

import Rambed360.entity.Talla;

public class InventarioRequest {

    // ID de la referencia del producto
    private Long referenciaId;

    // Talla del producto
    private Talla talla;

    // Cantidad disponible
    private Integer cantidad;

    // Precio unitario
    private BigDecimal precio;
    // Costo por reff
    private BigDecimal costo;

    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }

    public Talla getTalla() { return talla; }
    public void setTalla(Talla talla) { this.talla = talla; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
}
