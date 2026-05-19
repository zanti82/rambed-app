package Rambed360.dto.request;


import java.time.LocalDate;

public class FacturaRequest {

    // Numero de factura unico
    private String numeroFactura;

    // ID del cliente
    private Long clienteId;

    // ID del vendedor
    private Long vendedorId;

    // Fecha de emision de la factura
    private LocalDate fechaEmision;

    // Notas opcionales
    private String notas;

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
