package Rambed360.controller;

import Rambed360.dto.request.FacturaDetalleRequest;
import Rambed360.dto.response.FacturaDetalleResponse;
import Rambed360.service.FacturaDetalleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/factura-detalle")
public class FacturaDetalleController {

    // Servicio que contiene la logica de negocio
    private final FacturaDetalleService facturaDetalleService;

    // Constructor explicito con la dependencia del servicio
    public FacturaDetalleController(FacturaDetalleService facturaDetalleService) {
        this.facturaDetalleService = facturaDetalleService;
    }

    // Retorna todos los items de una factura como DTO
    @GetMapping("/factura/{facturaId}")
    public List<FacturaDetalleResponse> listarPorFactura(@PathVariable Long facturaId) {
        List<FacturaDetalleResponse> items = facturaDetalleService.listarPorFactura(facturaId);
        return items;
    }

    // Agrega un item a una factura recibiendo un DTO
    @PostMapping
    public ResponseEntity<FacturaDetalleResponse> agregar(@RequestBody FacturaDetalleRequest request) {
        FacturaDetalleResponse detalleGuardado = facturaDetalleService.agregar(request);
        return ResponseEntity.ok(detalleGuardado);
    }

    // Elimina un item de una factura y devuelve stock al inventario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaDetalleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}