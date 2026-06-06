package Rambed360.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "comisiones")
public class Comision {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Factura que genero esta comision, una comision por factura
    @OneToOne
    @JoinColumn(name = "factura_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Factura factura;

    // Vendedor al que se le paga la comision
    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vendedor vendedor;

    // Porcentaje de comision al momento de vender, 0 o 2
    @Column(name = "porc_comision_venta", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcComisionVenta = BigDecimal.ZERO;

    // Monto calculado de la comision de venta
    @Column(name = "monto_comision_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoComisionVenta = BigDecimal.ZERO;

    // Porcentaje de comision al pagar la factura, por defecto 4, editable
    // Es null hasta que la factura se marca como pagada
    @Column(name = "porc_comision_pago", precision = 5, scale = 2)
    private BigDecimal porcComisionPago;

    // Monto calculado de la comision de pago
    // Es null hasta que la factura se marca como pagada
    @Column(name = "monto_comision_pago", precision = 10, scale = 2)
    private BigDecimal montoComisionPago;

    // Indica si la comision fue liquidada al vendedor a fin de mes
    @Column(nullable = false)
    private Integer liquidada = 0;

    // Fecha en que el admin liquido la comision, null hasta que se liquide
    @Column(name = "fecha_liquidacion")
    private LocalDateTime fechaLiquidacion;

    // Fecha de creacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Fecha de ultima actualizacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "actualizado_en", insertable = false, updatable = false)
    private LocalDateTime actualizadoEn;

    // Constructor vacio obligatorio para JPA
    public Comision() {}

    // Constructor con campos obligatorios al crear la factura
    public Comision(Factura factura, Vendedor vendedor, BigDecimal porcComisionVenta, BigDecimal montoComisionVenta) {
        this.factura = factura;
        this.vendedor = vendedor;
        this.porcComisionVenta = porcComisionVenta;
        this.montoComisionVenta = montoComisionVenta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }

    public BigDecimal getPorcComisionVenta() { return porcComisionVenta; }
    public void setPorcComisionVenta(BigDecimal porcComisionVenta) { this.porcComisionVenta = porcComisionVenta; }

    public BigDecimal getMontoComisionVenta() { return montoComisionVenta; }
    public void setMontoComisionVenta(BigDecimal montoComisionVenta) { this.montoComisionVenta = montoComisionVenta; }

    public BigDecimal getPorcComisionPago() { return porcComisionPago; }
    public void setPorcComisionPago(BigDecimal porcComisionPago) { this.porcComisionPago = porcComisionPago; }

    public BigDecimal getMontoComisionPago() { return montoComisionPago; }
    public void setMontoComisionPago(BigDecimal montoComisionPago) { this.montoComisionPago = montoComisionPago; }

    public Integer getLiquidada() { return liquidada; }
    public void setLiquidada(Integer liquidada) { this.liquidada = liquidada; }

    public LocalDateTime getFechaLiquidacion() { return fechaLiquidacion; }
    public void setFechaLiquidacion(LocalDateTime fechaLiquidacion) { this.fechaLiquidacion = fechaLiquidacion; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
