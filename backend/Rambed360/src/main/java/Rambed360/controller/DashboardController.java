package Rambed360.controller;


import Rambed360.dto.response.DashboardResponse;
import Rambed360.entity.Usuario;
import Rambed360.repository.UsuarioRepository;
import Rambed360.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    // Servicio que contiene la logica del dashboard
    private final DashboardService dashboardService;

    // Repositorio para obtener el usuario autenticado
    private final UsuarioRepository usuarioRepository;

    // Constructor explicito con las dos dependencias
    public DashboardController(DashboardService dashboardService,
                               UsuarioRepository usuarioRepository) {
        this.dashboardService = dashboardService;
        this.usuarioRepository = usuarioRepository;
    }

    // Retorna los datos del dashboard segun el rol del usuario
    @GetMapping
    public ResponseEntity<DashboardResponse> obtenerDashboard() {

        // Obtiene el usuario autenticado desde el contexto de Spring Security
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        String username = autenticacion.getName();

        // Busca el usuario en la base de datos
        Optional<Usuario> usuarioResultado = usuarioRepository.findByUsername(username);

        // Si no existe lanza un error
        if (usuarioResultado.isPresent() == false) {
            return ResponseEntity.notFound().build();
        }

        // Guarda el usuario encontrado
        Usuario usuario = usuarioResultado.get();

        // Si es admin retorna el dashboard completo
        if (usuario.getRol().getNombre().equals("ADMIN")) {
            DashboardResponse respuesta = dashboardService.obtenerDashboardAdmin();
            return ResponseEntity.ok(respuesta);
        }

        // Si es vendedor retorna solo sus datos
        DashboardResponse respuesta = dashboardService.obtenerDashboardVendedor(usuario.getVendedor().getId());
        return ResponseEntity.ok(respuesta);
    }
}
