package Rambed360.controller;

import Rambed360.dto.request.InventarioRequest;
import Rambed360.dto.response.InventarioResponse;
import Rambed360.entity.Talla;
import Rambed360.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    // Servicio que contiene la logica de negocio
    private final InventarioService inventarioService;

    // Constructor explicito con la dependencia del servicio
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // Retorna todo el inventario como DTO
    @GetMapping
    public List<InventarioResponse> listarTodo() {
        List<InventarioResponse> inventario = inventarioService.listarTodo();
        return inventario;
    }

    // Retorna el stock de una referencia en todas sus tallas
    @GetMapping("/referencia/{referenciaId}")
    public List<InventarioResponse> listarPorReferencia(@PathVariable Long referenciaId) {
        List<InventarioResponse> inventario = inventarioService.listarPorReferencia(referenciaId);
        return inventario;
    }

    // Retorna solo los registros con stock disponible
    @GetMapping("/con-stock")
    public List<InventarioResponse> listarConStock() {
        List<InventarioResponse> inventario = inventarioService.listarConStock();
        return inventario;
    }

    // Busca un registro de inventario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> buscarPorId(@PathVariable Long id) {
        InventarioResponse inventario = inventarioService.buscarPorId(id);
        return ResponseEntity.ok(inventario);
    }

    // Busca un registro especifico por referencia y talla
    @GetMapping("/referencia/{referenciaId}/talla/{talla}")
    public ResponseEntity<InventarioResponse> buscarPorReferenciaYTalla(
            @PathVariable Long referenciaId,
            @PathVariable Talla talla) {
        InventarioResponse inventario = inventarioService.buscarPorReferenciaYTalla(referenciaId, talla);
        return ResponseEntity.ok(inventario);
    }

    // Crea un registro nuevo de inventario recibiendo un DTO
    @PostMapping
    public ResponseEntity<InventarioResponse> guardar(@RequestBody InventarioRequest request) {
        InventarioResponse inventarioGuardado = inventarioService.guardar(request);
        return ResponseEntity.ok(inventarioGuardado);
    }

    // Actualiza cantidad y precio de un registro
    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponse> actualizar(@PathVariable Long id, @RequestBody InventarioRequest request) {
        InventarioResponse inventarioActualizado = inventarioService.actualizar(id, request);
        return ResponseEntity.ok(inventarioActualizado);
    }

    // SOLO DESARROLLO - elimina fisicamente el registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}