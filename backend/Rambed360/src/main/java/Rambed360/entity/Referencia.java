package Rambed360.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referencias")
public class Referencia {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Marca del producto, campo obligatorio
    @Column(nullable = false, length = 100)
    private String marca;

    // Referencia o codigo del producto, campo obligatorio
    @Column(nullable = false, length = 100)
    private String referencia;

    // Descripcion opcional del producto
    @Column(length = 255)
    private String descripcion;

    // Estado activo o inactivo de la referencia, por defecto activo
    @Column(nullable = false)
    private Byte activo = 1;

    // Fecha de creacion, la maneja la base de datos
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Fecha de ultima actualizacion, la maneja la base de datos
    @Column(name = "actualizado_en", insertable = false, updatable = false)
    private LocalDateTime actualizadoEn;

    // Constructor vacio obligatorio para JPA
    public Referencia() {}

    // Constructor con campos obligatorios
    public Referencia(String marca, String referencia, String descripcion) {
        this.marca = marca;
        this.referencia = referencia;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Byte getActivo() { return activo; }
    public void setActivo(Byte activo) { this.activo = activo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
