package Rambed360.dto.request;

// Datos que el frontend envia para hacer login
public class LoginRequest {

    // Nombre de usuario
    private String username;

    // Contrasena sin encriptar
    private String password;

    public LoginRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
