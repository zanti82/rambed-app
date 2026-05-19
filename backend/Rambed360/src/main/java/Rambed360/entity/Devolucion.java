package Rambed360.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "devoluciones")
public class Devolucion {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Item de la factura que se esta devolviendo
    @ManyToOne
    @JoinColumn(name = "factura_detalle_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "factura", "inventario"})
    private FacturaDetalle facturaDetalle;

    // Cantidad de unidades que se devuelven
    @Column(nullable = false)
    private Integer cantidad;

    // Motivo de la devolucion, opcional
    @Column(columnDefinition = "TEXT")
    private String motivo;

    // Fecha de creacion, la maneja la base de datos
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Constructor vacio obligatorio para JPA
    public Devolucion() {}

    // Constructor con campos obligatorios
    public Devolucion(FacturaDetalle facturaDetalle, Integer cantidad, String motivo) {
        this.facturaDetalle = facturaDetalle;
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FacturaDetalle getFacturaDetalle() { return facturaDetalle; }
    public void setFacturaDetalle(FacturaDetalle facturaDetalle) { this.facturaDetalle = facturaDetalle; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}