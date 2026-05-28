package Rambed360.dto.response;


// Datos que el backend envia al frontend sobre un usuario
public class UsuarioResponse {

    // Identificador del usuario
    private Long id;

    // Nombre de usuario
    private String username;

    // Nombre del rol
    private String rol;

    // ID del vendedor asociado si aplica
    private Long vendedorId;

    // Nombre del vendedor asociado si aplica
    private String vendedorNombre;

    // Estado activo o inactivo
    private Byte activo;

    public UsuarioResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }

    public String getVendedorNombre() { return vendedorNombre; }
    public void setVendedorNombre(String vendedorNombre) { this.vendedorNombre = vendedorNombre; }

    public Byte getActivo() { return activo; }
    public void setActivo(Byte activo) { this.activo = activo; }
}
