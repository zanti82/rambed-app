package Rambed360.dto.request;


// Datos que el frontend envia para crear un usuario
public class UsuarioRequest {

    // Nombre de usuario para login
    private String username;

    // Contrasena sin encriptar
    private String password;

    // ID del rol: 1=ADMIN, 2=VENDEDOR
    private Long rolId;

    // ID del vendedor asociado, solo si el rol es VENDEDOR
    private Long vendedorId;

    public UsuarioRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }
}
