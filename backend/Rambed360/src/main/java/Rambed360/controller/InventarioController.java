package Rambed360.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Rambed360.entity.Inventario;
import Rambed360.entity.Talla;
import Rambed360.service.InventarioService;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    // Servicio que contiene la logica de negocio
    private final InventarioService inventarioService;

    // Constructor explicito con la dependencia del servicio
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // Retorna todo el inventario
    @GetMapping
    public List<Inventario> listarTodo() {
        return inventarioService.listarTodo();
    }

    // Retorna el stock de una referencia en todas sus tallas
    @GetMapping("/referencia/{referenciaId}")
    public List<Inventario> listarPorReferencia(@PathVariable Long referenciaId) {
        return inventarioService.listarPorReferencia(referenciaId);
    }

    // Retorna solo los registros con stock disponible
    @GetMapping("/con-stock")
    public List<Inventario> listarConStock() {
        return inventarioService.listarConStock();
    }

    // Busca un registro de inventario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.buscarPorId(id);
        return ResponseEntity.ok(inventario);
    }

    // Crea un registro nuevo de inventario
    @PostMapping
    public ResponseEntity<Inventario> guardar(@RequestBody Inventario inventario) {
        Inventario inventarioGuardado = inventarioService.guardar(inventario);
        return ResponseEntity.ok(inventarioGuardado);
    }

    // Actualiza cantidad y precio de un registro
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        Inventario inventarioActualizado = inventarioService.actualizar(id, inventario);
        return ResponseEntity.ok(inventarioActualizado);
    }

    // SOLO DESARROLLO - elimina fisicamente el registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

        // Busca un registro especifico por referencia y talla
    @GetMapping("/referencia/{referenciaId}/talla/{talla}")
    public ResponseEntity<Inventario> buscarPorReferenciaYTalla(
            @PathVariable Long referenciaId,
            @PathVariable Talla talla) {

        Inventario inventario = inventarioService.buscarPorReferenciaYTalla(referenciaId, talla);
        return ResponseEntity.ok(inventario);
    }
}
