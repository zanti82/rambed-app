package Rambed360.dto.request;


import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaRequest {

   
    // ID del cliente
    private Long clienteId;

    // ID del vendedor
    private Long vendedorId;
    
    //porcentaje de la comision
    private BigDecimal porcComisionVenta;

    // Fecha de emision de la factura
    private LocalDate fechaEmision;

    // Notas opcionales
    private String notas;

    
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public BigDecimal getPorcComisionVenta() {
        return porcComisionVenta;
    }
    public void setPorcComisionVenta(BigDecimal porcComisionVenta) {
        this.porcComisionVenta = porcComisionVenta;
    }

    
}
