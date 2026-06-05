package Rambed360.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import Rambed360.entity.EstadoFactura;

public class FacturaResponse {

    // ID de la factura
    private Long id;

    // Numero de factura
    private String numeroFactura;

    // ID del cliente
    private Long clienteId;

    // Nombre del cliente
    private String clienteNombre;

    private String clienteDireccion;

    private String clienteTelefono;

    // ID del vendedor
    private Long vendedorId;

    // Nombre del vendedor
    private String vendedorNombre;

    // Fecha de emision
    private LocalDate fechaEmision;

    // Fecha de pago, opcional
    private LocalDate fechaPago;

    // Porcentaje de descuento aplicado
    private BigDecimal descuentoPorcentaje;

    // Subtotal antes del descuento
    private BigDecimal subtotal;

    // Total final despues del descuento
    private BigDecimal total;

     // Suma acumulada de abonos recibidos
    private BigDecimal totalPagado;
    // Porcentaje de comision de venta, 0 o 2, opcional
    private BigDecimal porcComisionVenta;

    // Estado de la factura
    private EstadoFactura estado;

    // Notas opcionales
    private String notas;

    // Fecha de creacion
    private LocalDateTime creadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

   
    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }

    public String getVendedorNombre() { return vendedorNombre; }
    public void setVendedorNombre(String vendedorNombre) { this.vendedorNombre = vendedorNombre; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public BigDecimal getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public BigDecimal getTotalPagado() { return totalPagado; }
    public void setTotalPagado(BigDecimal totalPagado) { this.totalPagado = totalPagado; }

    public BigDecimal getPorcComisionVenta() { return porcComisionVenta; }
    public void setPorcComisionVenta(BigDecimal porcComisionVenta) { this.porcComisionVenta = porcComisionVenta; }
    
    public String getClienteDireccion() {
        return clienteDireccion;
    }
    
    public void setClienteDireccion(String clienteDireccion) {
        this.clienteDireccion = clienteDireccion;
    }
    public String getClienteTelefono() {
        return clienteTelefono;
    }
    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    
}