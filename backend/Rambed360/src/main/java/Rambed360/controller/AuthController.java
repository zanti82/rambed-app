package Rambed360.controller;

import Rambed360.dto.request.LoginRequest;
import Rambed360.dto.response.LoginResponse;
import Rambed360.entity.Usuario;
import Rambed360.repository.UsuarioRepository;
import Rambed360.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Maneja la autenticacion con Spring Security
    private final AuthenticationManager authenticationManager;

    // Utilidad para generar el token JWT
    private final JwtUtil jwtUtil;

    // Repositorio para obtener los datos del usuario despues del login
    private final UsuarioRepository usuarioRepository;

    // Constructor explicito con las tres dependencias
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    // Endpoint de login que recibe username y password y devuelve un token JWT
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // Crea el objeto de autenticacion con username y password
        UsernamePasswordAuthenticationToken credenciales = new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        );

        // Autentica al usuario con Spring Security
        // Si las credenciales son incorrectas lanza una excepcion automaticamente
        Authentication autenticacion = authenticationManager.authenticate(credenciales);

        // Busca el usuario completo en la base de datos para obtener el rol y vendedorId
        Optional<Usuario> usuarioResultado = usuarioRepository.findByUsername(request.getUsername());

        // Guarda el usuario encontrado
        Usuario usuario = usuarioResultado.get();

        // Obtiene el nombre del rol del usuario
        String nombreRol = usuario.getRol().getNombre();

        // Genera el token JWT con el username y el rol
        String token = jwtUtil.generarToken(request.getUsername(), nombreRol);

        // Obtiene el vendedorId si el usuario es VENDEDOR, null si es ADMIN
        Long vendedorId = null;
        if (usuario.getVendedor() != null) {
            vendedorId = usuario.getVendedor().getId();
        }

        // Crea la respuesta con el token y los datos del usuario
        LoginResponse respuesta = new LoginResponse(token, usuario.getUsername(), nombreRol, vendedorId);

        return ResponseEntity.ok(respuesta);
    }
}