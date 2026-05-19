package Rambed360.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {

    // Identificador unico autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del rol: ADMIN o VENDEDOR
    @Column(nullable = false, unique = true, length = 20)
    private String nombre;

    // Constructor vacio obligatorio para JPA
    public Rol() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}