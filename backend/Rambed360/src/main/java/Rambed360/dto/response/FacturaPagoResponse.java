package Rambed360.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaPagoResponse {
    // Id del abono
    private Long id;
    // Id de la factura
    private Long facturaId;
    // Numero de factura para mostrar en pantalla
    private String numeroFactura;
    // Monto abonado
    private BigDecimal monto;
    // Fecha del abono
    private LocalDate fechaPago;
    // Notas del abono
    private String notas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}