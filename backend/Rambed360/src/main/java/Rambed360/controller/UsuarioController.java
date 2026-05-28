package Rambed360.controller;

import Rambed360.dto.request.CambiarPasswordRequest;
import Rambed360.dto.request.UsuarioRequest;
import Rambed360.dto.response.UsuarioResponse;
import Rambed360.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Servicio que contiene la logica de negocio
    private final UsuarioService usuarioService;

    // Constructor explicito con la dependencia del servicio
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Retorna todos los usuarios
    @GetMapping
    public List<UsuarioResponse> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return usuarios;
    }

    // Busca un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // Crea un usuario nuevo
    @PostMapping
    public ResponseEntity<UsuarioResponse> guardar(@RequestBody UsuarioRequest request) {
        UsuarioResponse usuarioGuardado = usuarioService.guardar(request);
        return ResponseEntity.ok(usuarioGuardado);
    }

    // Cambia el password de un usuario
    @PatchMapping("/{id}/password")
    public ResponseEntity<UsuarioResponse> cambiarPassword(@PathVariable Long id,
                                                            @RequestBody CambiarPasswordRequest request) {
        UsuarioResponse usuarioActualizado = usuarioService.cambiarPassword(id, request);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Activa un usuario
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UsuarioResponse> activar(@PathVariable Long id) {
        UsuarioResponse usuarioActivado = usuarioService.activar(id);
        return ResponseEntity.ok(usuarioActivado);
    }

    // Desactiva un usuario
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponse> desactivar(@PathVariable Long id) {
        UsuarioResponse usuarioDesactivado = usuarioService.desactivar(id);
        return ResponseEntity.ok(usuarioDesactivado);
    }
}
