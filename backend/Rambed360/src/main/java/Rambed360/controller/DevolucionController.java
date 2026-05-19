package Rambed360.controller;

import Rambed360.dto.request.DevolucionRequest;
import Rambed360.dto.response.DevolucionResponse;
import Rambed360.service.DevolucionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devoluciones")
public class DevolucionController {

    // Servicio que contiene la logica de negocio
    private final DevolucionService devolucionService;

    // Constructor explicito con la dependencia del servicio
    public DevolucionController(DevolucionService devolucionService) {
        this.devolucionService = devolucionService;
    }

    // Retorna todas las devoluciones como DTO
    @GetMapping
    public List<DevolucionResponse> listarTodas() {
        List<DevolucionResponse> devoluciones = devolucionService.listarTodas();
        return devoluciones;
    }

    // Retorna las devoluciones de un item de factura especifico
    @GetMapping("/factura-detalle/{facturaDetalleId}")
    public List<DevolucionResponse> listarPorFacturaDetalle(@PathVariable Long facturaDetalleId) {
        List<DevolucionResponse> devoluciones = devolucionService.listarPorFacturaDetalle(facturaDetalleId);
        return devoluciones;
    }

    // Busca una devolucion por su ID
    @GetMapping("/{id}")
    public ResponseEntity<DevolucionResponse> buscarPorId(@PathVariable Long id) {
        DevolucionResponse devolucion = devolucionService.buscarPorId(id);
        return ResponseEntity.ok(devolucion);
    }

    // Registra una devolucion nueva recibiendo un DTO
    @PostMapping
    public ResponseEntity<DevolucionResponse> registrar(@RequestBody DevolucionRequest request) {
        DevolucionResponse devolucionRegistrada = devolucionService.registrar(request);
        return ResponseEntity.ok(devolucionRegistrada);
    }
}