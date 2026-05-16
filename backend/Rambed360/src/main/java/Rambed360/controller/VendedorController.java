package Rambed360.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Rambed360.entity.Vendedor;
import Rambed360.service.VendedorService;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final VendedorService vendedorService;

    

    public VendedorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @GetMapping
    public List<Vendedor> listarTodos() {
        return vendedorService.listarTodos();
    }

    @GetMapping("/activos")
    public List<Vendedor> listarActivos() {
        return vendedorService.listarActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> buscarPorId(@PathVariable Long id) {

        Vendedor vendedor = vendedorService.buscarPorId(id);
        return ResponseEntity.ok(vendedor);
    }

    @PostMapping
    public ResponseEntity<Vendedor> guardar(@RequestBody Vendedor vendedor) {

        Vendedor vendedorNew = vendedorService.guardar(vendedor);
        return ResponseEntity.ok(vendedorNew);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> actualizar(@PathVariable Long id, @RequestBody Vendedor vendedor) {
        
        Vendedor vendedorUpdate = vendedorService.actualizar(id, vendedor);
        return ResponseEntity.ok(vendedorUpdate);
    }


        // Activar una vendedor
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Vendedor> activar(@PathVariable Long id) {
        Vendedor vendedorActivado = vendedorService.activar(id);
        return ResponseEntity.ok(vendedorActivado);
    }

    // Activar una vendedor
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Vendedor> desactivar(@PathVariable Long id) {
        Vendedor vendedorDesactivado = vendedorService.desactivar(id);
        return ResponseEntity.ok(vendedorDesactivado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vendedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
