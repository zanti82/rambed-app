package Rambed360.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaPagoRequest {
    // Id de la factura a abonar
    private Long facturaId;
    // Monto del abono
    private BigDecimal monto;
    // Fecha del abono
    private LocalDate fechaPago;
    // Notas opcionales
    private String notas;

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}