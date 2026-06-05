package Rambed360.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComisionResponse {
    // Id de la comision
    private Long id;
    // Id de la factura
    private Long facturaId;
    // Numero de factura para mostrar en pantalla
    private String numeroFactura;
    // Nombre del vendedor
    private String nombreVendedor;
    // Total de la factura
    private BigDecimal totalFactura;
    // Porcentaje y monto de comision de venta
    private BigDecimal porcComisionVenta;
    private BigDecimal montoComisionVenta;
    // Porcentaje y monto de comision de pago, null hasta que se pague
    private BigDecimal porcComisionPago;
    private BigDecimal montoComisionPago;
    // Estado de liquidacion
    private Integer liquidada;
    private LocalDateTime fechaLiquidacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getNombreVendedor() { return nombreVendedor; }
    public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }

    public BigDecimal getTotalFactura() { return totalFactura; }
    public void setTotalFactura(BigDecimal totalFactura) { this.totalFactura = totalFactura; }

    public BigDecimal getPorcComisionVenta() { return porcComisionVenta; }
    public void setPorcComisionVenta(BigDecimal porcComisionVenta) { this.porcComisionVenta = porcComisionVenta; }

    public BigDecimal getMontoComisionVenta() { return montoComisionVenta; }
    public void setMontoComisionVenta(BigDecimal montoComisionVenta) { this.montoComisionVenta = montoComisionVenta; }

    public BigDecimal getPorcComisionPago() { return porcComisionPago; }
    public void setPorcComisionPago(BigDecimal porcComisionPago) { this.porcComisionPago = porcComisionPago; }

    public BigDecimal getMontoComisionPago() { return montoComisionPago; }
    public void setMontoComisionPago(BigDecimal montoComisionPago) { this.montoComisionPago = montoComisionPago; }

    public Integer getLiquidada() { return liquidada; }
    public void setLiquidada(Integer liquidada) { this.liquidada = liquidada; }

    public LocalDateTime getFechaLiquidacion() { return fechaLiquidacion; }
    public void setFechaLiquidacion(LocalDateTime fechaLiquidacion) { this.fechaLiquidacion = fechaLiquidacion; }
}