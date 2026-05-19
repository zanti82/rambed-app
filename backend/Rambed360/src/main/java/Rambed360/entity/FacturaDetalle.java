package Rambed360.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Factura a la que pertenece este detalle
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
     @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "cliente", "vendedor", "notas"})
    private Factura factura;

    // Registro de inventario que se esta vendiendo
    @ManyToOne
    @JoinColumn(name = "inventario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Inventario inventario;

    // Cantidad de unidades vendidas
    @Column(nullable = false)
    private Integer cantidad;

    // Precio unitario al momento de la venta
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    // Fecha de creacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Constructor vacio obligatorio para JPA
    public FacturaDetalle() {}

    // Constructor con campos obligatorios
    public FacturaDetalle(Factura factura, Inventario inventario, Integer cantidad, BigDecimal precioUnitario) {
        this.factura = factura;
        this.inventario = inventario;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public Inventario getInventario() { return inventario; }
    public void setInventario(Inventario inventario) { this.inventario = inventario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
