package Rambed360.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del cliente, campo obligatorio
    @Column(nullable = false, length = 100)
    private String nombre;

    // Identificacion unica del cliente, puede contener letras y numeros
    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;

    // Ciudad donde esta ubicado el cliente, campo obligatorio
    @Column(nullable = false, length = 100)
    private String ciudad;

    // Direccion del cliente, campo obligatorio
    @Column(nullable = false, length = 255)
    private String direccion;

    // Correo electronico del cliente, opcional
    @Column(length = 100)
    private String correo;

    // Telefono del cliente, campo obligatorio
    @Column(nullable = false, length = 20)
    private String telefono;

    // Nombre del almacen del cliente, campo obligatorio
    @Column(name = "nombre_almacen", nullable = false, length = 100)
    private String nombreAlmacen;

    // Vendedor asignado al cliente
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    // Instagram del cliente, opcional
    @Column(length = 100)
    private String instagram;

    // Facebook del cliente, opcional
    @Column(length = 100)
    private String facebook;

    // Whatsapp del cliente, opcional
    @Column(length = 20)
    private String whatsapp;

    // Estado activo o inactivo del cliente, por defecto activo
    @Column(nullable = false)
    private Byte activo = 1;

    // Fecha de creacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Fecha de ultima actualizacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "actualizado_en", insertable = false, updatable = false)
    private LocalDateTime actualizadoEn;

    // Constructor vacio obligatorio para JPA
    public Cliente() {}

    // Constructor con campos obligatorios
    public Cliente(String nombre, String identificacion, String ciudad, String direccion,
                   String telefono, String nombreAlmacen, Vendedor vendedor) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreAlmacen = nombreAlmacen;
        this.vendedor = vendedor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNombreAlmacen() { return nombreAlmacen; }
    public void setNombreAlmacen(String nombreAlmacen) { this.nombreAlmacen = nombreAlmacen; }

    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }

    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getFacebook() { return facebook; }
    public void setFacebook(String facebook) { this.facebook = facebook; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public Byte getActivo() { return activo; }
    public void setActivo(Byte activo) { this.activo = activo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}