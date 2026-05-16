package Rambed360.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Rambed360.entity.FacturaDetalle;
import Rambed360.service.FacturaDetalleService;

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

    // Retorna todos los items de una factura
    @GetMapping("/factura/{facturaId}")
    public List<FacturaDetalle> listarPorFactura(@PathVariable Long facturaId) {
        List<FacturaDetalle> itemsDeFactura = facturaDetalleService.listarPorFactura(facturaId);
        return itemsDeFactura;
    }

    // Agrega un item a una factura
    @PostMapping
    public ResponseEntity<FacturaDetalle> agregar(@RequestBody FacturaDetalle detalle) {
        FacturaDetalle detalleGuardado = facturaDetalleService.agregar(detalle);
        return ResponseEntity.ok(detalleGuardado);
    }

    // Elimina un item de una factura y devuelve stock al inventario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaDetalleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
