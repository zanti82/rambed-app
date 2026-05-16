package Rambed360.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Rambed360.entity.EstadoFactura;
import Rambed360.entity.Factura;
import Rambed360.service.FacturaService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    // Servicio que contiene la logica de negocio
    private final FacturaService facturaService;

    // Constructor explicito con la dependencia del servicio
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // Retorna todas las facturas
    @GetMapping
    public List<Factura> listarTodas() {
        List<Factura> todasLasFacturas = facturaService.listarTodas();
        return todasLasFacturas;
    }

    // Retorna facturas filtradas por estado
    @GetMapping("/estado/{estado}")
    public List<Factura> listarPorEstado(@PathVariable EstadoFactura estado) {
        List<Factura> facturasPorEstado = facturaService.listarPorEstado(estado);
        return facturasPorEstado;
    }

    // Retorna facturas de un cliente especifico
    @GetMapping("/cliente/{clienteId}")
    public List<Factura> listarPorCliente(@PathVariable Long clienteId) {
        List<Factura> facturasPorCliente = facturaService.listarPorCliente(clienteId);
        return facturasPorCliente;
    }

    // Retorna facturas de un vendedor especifico
    @GetMapping("/vendedor/{vendedorId}")
    public List<Factura> listarPorVendedor(@PathVariable Long vendedorId) {
        List<Factura> facturasPorVendedor = facturaService.listarPorVendedor(vendedorId);
        return facturasPorVendedor;
    }

    // Busca una factura por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Factura> buscarPorId(@PathVariable Long id) {
        Factura facturaEncontrada = facturaService.buscarPorId(id);
        return ResponseEntity.ok(facturaEncontrada);
    }

    // Crea una factura nueva
    @PostMapping
    public ResponseEntity<Factura> guardar(@RequestBody Factura factura) {
        Factura facturaGuardada = facturaService.guardar(factura);
        return ResponseEntity.ok(facturaGuardada);
    }

    // Registra el pago de una factura con descuento opcional
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Factura> registrarPago(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal descuento) {

        Factura facturaPagada = facturaService.registrarPago(id, descuento);
        return ResponseEntity.ok(facturaPagada);
    }

    // Anula una factura
    @PatchMapping("/{id}/anular")
    public ResponseEntity<Factura> anular(@PathVariable Long id) {
        Factura facturaAnulada = facturaService.anular(id);
        return ResponseEntity.ok(facturaAnulada);
    }

    // SOLO DESARROLLO - elimina fisicamente la factura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}