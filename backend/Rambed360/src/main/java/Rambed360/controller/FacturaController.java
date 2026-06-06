package Rambed360.controller;

import Rambed360.dto.request.FacturaRequest;
import Rambed360.dto.request.PagoRequest;
import Rambed360.dto.response.FacturaResponse;
import Rambed360.entity.EstadoFactura;
import Rambed360.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import Rambed360.dto.request.FacturaPagoRequest;
import Rambed360.dto.response.FacturaPagoResponse;
import Rambed360.dto.response.ComisionResponse;


@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    // Servicio que contiene la logica de negocio
    private final FacturaService facturaService;

    // Constructor explicito con la dependencia del servicio
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // Retorna todas las facturas como DTO
    @GetMapping
    public List<FacturaResponse> listarTodas() {
        List<FacturaResponse> facturas = facturaService.listarTodas();
        return facturas;
    }

    // Retorna facturas filtradas por estado
    @GetMapping("/estado/{estado}")
    public List<FacturaResponse> listarPorEstado(@PathVariable EstadoFactura estado) {
        List<FacturaResponse> facturas = facturaService.listarPorEstado(estado);
        return facturas;
    }

    // Retorna facturas de un cliente especifico
    @GetMapping("/cliente/{clienteId}")
    public List<FacturaResponse> listarPorCliente(@PathVariable Long clienteId) {
        List<FacturaResponse> facturas = facturaService.listarPorCliente(clienteId);
        return facturas;
    }

    // Retorna facturas de un vendedor especifico
    @GetMapping("/vendedor/{vendedorId}")
    public List<FacturaResponse> listarPorVendedor(@PathVariable Long vendedorId) {
        List<FacturaResponse> facturas = facturaService.listarPorVendedor(vendedorId);
        return facturas;
    }

    // Busca una factura por su ID
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponse> buscarPorId(@PathVariable Long id) {
        FacturaResponse factura = facturaService.buscarPorId(id);
        return ResponseEntity.ok(factura);
    }

    // Crea una factura nueva recibiendo un DTO
    @PostMapping
    public ResponseEntity<FacturaResponse> guardar(@RequestBody FacturaRequest request) {
        FacturaResponse facturaGuardada = facturaService.guardar(request);
        return ResponseEntity.ok(facturaGuardada);
    }

    // Registra el pago de una factura con descuento opcional
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<FacturaResponse> registrarPago(
            @PathVariable Long id,
            @RequestBody PagoRequest request) {
        FacturaResponse facturaPagada = facturaService.registrarPago(id, request);
        return ResponseEntity.ok(facturaPagada);
    }

    // Anula una factura y devuelve stock al inventario
    @PatchMapping("/{id}/anular")
    public ResponseEntity<FacturaResponse> anular(@PathVariable Long id) {
        FacturaResponse facturaAnulada = facturaService.anular(id);
        return ResponseEntity.ok(facturaAnulada);
    }

        // Registra un abono parcial a una factura
    @PostMapping("/abonos")
    public ResponseEntity<FacturaPagoResponse> registrarAbono(@RequestBody FacturaPagoRequest request) {
        FacturaPagoResponse abono = facturaService.registrarAbono(request);
        return ResponseEntity.ok(abono);
    }

    // Retorna todos los abonos de una factura especifica
    @GetMapping("/{facturaId}/abonos")
    public List<FacturaPagoResponse> listarAbonos(@PathVariable Long facturaId) {
        List<FacturaPagoResponse> abonos = facturaService.listarAbonosPorFactura(facturaId);
        return abonos;
    }

    // Retorna comisiones, filtradas por liquidada si viene el parametro
    @GetMapping("/comisiones")
    public List<ComisionResponse> listarComisiones(@RequestParam(required = false) Integer liquidada) {
        List<ComisionResponse> comisiones = facturaService.listarComisiones(liquidada);
        return comisiones;
    }

    // Liquida todas las comisiones pendientes de un vendedor
    @PatchMapping("/comisiones/liquidar/{vendedorId}")
    public List<ComisionResponse> liquidarComisiones(@PathVariable Long vendedorId) {
        List<ComisionResponse> liquidadas = facturaService.liquidarComisiones(vendedorId);
        return liquidadas;
    }

    // SOLO DESARROLLO - elimina fisicamente la factura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}