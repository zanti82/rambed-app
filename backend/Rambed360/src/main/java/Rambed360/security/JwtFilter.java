package Rambed360.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Utilidad para validar y extraer datos del token
    private final JwtUtil jwtUtil;

    // Servicio para cargar el usuario desde la base de datos
    private final UserDetailsServiceImpl userDetailsService;

    // Constructor explicito con las dos dependencias
    public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Se ejecuta en cada peticion HTTP para validar el token
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Obtiene el header Authorization de la peticion
        String headerAutorizacion = request.getHeader("Authorization");

        // Variable para guardar el token limpio
        String token = null;

        // Variable para guardar el username extraido del token
        String username = null;

        // Verifica que el header exista y empiece con Bearer
        if (headerAutorizacion != null && headerAutorizacion.startsWith("Bearer ")) {

            // Extrae el token quitando el prefijo Bearer
            token = headerAutorizacion.substring(7);

            // Extrae el username del token
            username = jwtUtil.extraerUsername(token);
        }

        // Si hay username y no hay autenticacion activa en el contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carga el usuario desde la base de datos
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Valida que el token sea correcto
            if (jwtUtil.validarToken(token)) {

                // Crea el objeto de autenticacion con el usuario y sus permisos
                UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                // Agrega los detalles de la peticion a la autenticacion
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Registra la autenticacion en el contexto de Spring Security
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        }

        // Continua con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}