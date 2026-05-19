package Rambed360.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta para firmar el token
    private final Key claveSecreta = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiracion del token: 8 horas en milisegundos
    private final long tiempoExpiracion = 8 * 60 * 60 * 1000;

    // Genera un token JWT con el username y el rol del usuario
    public String generarToken(String username, String rol) {

        // Construye el token con el username, rol y fecha de expiracion
        String token = Jwts.builder()
            .setSubject(username)
            .claim("rol", rol)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
            .signWith(claveSecreta)
            .compact();

        return token;
    }

    // Extrae el username del token
    public String extraerUsername(String token) {
        // Obtiene todos los claims del token
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(claveSecreta)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    // Extrae el rol del token
    public String extraerRol(String token) {
        // Obtiene todos los claims del token
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(claveSecreta)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.get("rol", String.class);
    }

    // Valida que el token sea correcto y no haya expirado
    public boolean validarToken(String token) {
        try {
            // Intenta parsear el token, si falla lanza una excepcion
            Jwts.parserBuilder()
                .setSigningKey(claveSecreta)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Si hay cualquier error el token no es valido
            return false;
        }
    }
}