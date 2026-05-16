package Rambed360.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Rambed360.entity.Referencia;
import Rambed360.service.ReferenciaService;

import java.util.List;

@RestController
@RequestMapping("/api/referencias")
public class ReferenciaController {

    // Servicio que contiene la logica de negocio
    private final ReferenciaService referenciaService;
    

    public ReferenciaController(ReferenciaService referenciaService) {
        this.referenciaService = referenciaService;
    }

    // Retorna todas las referencias
    @GetMapping
    public List<Referencia> listarTodas() {
        return referenciaService.listarTodas();
    }

    // Retorna referencias filtradas por marca
    @GetMapping("/marca/{marca}")
    public List<Referencia> listarPorMarca(@PathVariable String marca) {
        return referenciaService.listarPorMarca(marca);
    }

    // Busca una referencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Referencia> buscarPorId(@PathVariable Long id) {

        Referencia referenciaId = referenciaService.buscarPorId(id);
        return ResponseEntity.ok(referenciaId);
    }

    // Crea una referencia nueva
    @PostMapping
    public ResponseEntity<Referencia> guardar(@RequestBody Referencia referencia) {

        Referencia referenciaGuardada = referenciaService.guardar(referencia);
        return ResponseEntity.ok(referenciaGuardada);
       
    }

    // Actualiza los datos de una referencia
    @PutMapping("/{id}")
    public ResponseEntity<Referencia> actualizar(@PathVariable Long id, @RequestBody Referencia referencia) {
        
        Referencia referenciaUpdate = referenciaService.actualizar(id, referencia);
        return ResponseEntity.ok(referenciaUpdate);
       
    }
    // Retorna solo las referencias activas
    @GetMapping("/activas")
    public List<Referencia> listarActivas() {
        return referenciaService.listarActivas();
    }

    // Retorna referencias activas filtradas por marca
    @GetMapping("/activas/marca/{marca}")
    public List<Referencia> listarActivasPorMarca(@PathVariable String marca) {
        return referenciaService.listarActivasPorMarca(marca);
    }

    // Activa una referencia
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Referencia> activar(@PathVariable Long id) {

        Referencia referenciaActivar = referenciaService.activar(id);
        return ResponseEntity.ok(referenciaActivar);
       
    }

    // Desactiva una referencia
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Referencia> desactivar(@PathVariable Long id) {
        Referencia referenciaDesactivar = referenciaService.desactivar(id);
        return ResponseEntity.ok(referenciaDesactivar);
    }

    // SOLO DESARROLLO - elimina fisicamente la referencia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        referenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

  
}
    
