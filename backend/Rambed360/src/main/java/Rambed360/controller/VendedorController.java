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
        return ResponseEntity.ok(vendedorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Vendedor> guardar(@RequestBody Vendedor vendedor) {
        return ResponseEntity.ok(vendedorService.guardar(vendedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> actualizar(@PathVariable Long id, @RequestBody Vendedor vendedor) {
        return ResponseEntity.ok(vendedorService.actualizar(id, vendedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vendedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
