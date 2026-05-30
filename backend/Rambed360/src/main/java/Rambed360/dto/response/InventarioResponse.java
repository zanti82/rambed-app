package Rambed360.dto.response;



import java.math.BigDecimal;
import java.time.LocalDateTime;

import Rambed360.entity.Talla;

public class InventarioResponse {

    // ID del registro de inventario
    private Long id;

    // ID de la referencia
    private Long referenciaId;

    // Marca del producto
    private String marca;

    // Nombre de la referencia
    private String referencia;

    // Talla del producto
    private Talla talla;

    // Cantidad disponible
    private Integer cantidad;

    // Precio unitario
    private BigDecimal precio;
    
    //csoto unitario
    private BigDecimal costo;

    // Fecha de ultima actualizacion
    private LocalDateTime actualizadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Talla getTalla() { return talla; }
    public void setTalla(Talla talla) { this.talla = talla; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    public BigDecimal getCosto() { return costo; }
public void setCosto(BigDecimal costo) { this.costo = costo; }
}