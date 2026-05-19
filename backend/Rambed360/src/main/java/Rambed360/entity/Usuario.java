package Rambed360.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de usuario para login
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Contrasena encriptada con BCrypt, no se expone al frontend
    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    // Estado activo o inactivo del usuario
    @Column(nullable = false)
    private Byte activo = 1;

    // Rol asignado al usuario
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rol rol;

    // Vendedor asociado al usuario, solo aplica para rol VENDEDOR
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vendedor vendedor;

    // Fecha de creacion, la maneja la base de datos
    @JsonIgnore
    @Column(name = "creado_en", updatable = false, insertable = false)
    private LocalDateTime creadoEn;

    // Constructor vacio obligatorio para JPA
    public Usuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Byte getActivo() { return activo; }
    public void setActivo(Byte activo) { this.activo = activo; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}