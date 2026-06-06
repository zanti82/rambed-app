package Rambed360.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "factura_pagos")
public class FacturaPago {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Factura a la que pertenece este abono
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Factura factura;

    // Monto abonado en este pago
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    // Fecha en que se realizo el abono
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    // Notas opcionales del abono
    @Column(columnDefinition = "TEXT")
    private String notas;

    // Fecha de creacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Constructor vacio obligatorio para JPA
    public FacturaPago() {}

    // Constructor con campos obligatorios
    public FacturaPago(Factura factura, BigDecimal monto, LocalDate fechaPago) {
        this.factura = factura;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}