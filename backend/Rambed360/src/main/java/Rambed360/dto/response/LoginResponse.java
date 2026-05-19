package Rambed360.dto.response;

// Datos que el backend devuelve despues de un login exitoso
public class LoginResponse {

    // Token JWT para autenticar las siguientes peticiones
    private String token;

    // Username del usuario autenticado
    private String username;

    // Rol del usuario autenticado
    private String rol;

    // ID del vendedor si el usuario es VENDEDOR, null si es ADMIN
    private Long vendedorId;

    public LoginResponse() {}

    public LoginResponse(String token, String username, String rol, Long vendedorId) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.vendedorId = vendedorId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }
}