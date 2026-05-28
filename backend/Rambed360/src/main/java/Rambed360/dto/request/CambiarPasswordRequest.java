package Rambed360.dto.request;


// Datos para cambiar la contrasena de un usuario
public class CambiarPasswordRequest {

    // Nueva contrasena sin encriptar
    private String nuevaPassword;

    public CambiarPasswordRequest() {}

    public String getNuevaPassword() { return nuevaPassword; }
    public void setNuevaPassword(String nuevaPassword) { this.nuevaPassword = nuevaPassword; }
}