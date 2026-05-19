package Rambed360.dto.request;


import java.math.BigDecimal;

public class PagoRequest {

    // Porcentaje de descuento opcional al momento del pago
    private BigDecimal descuentoPorcentaje;

    public BigDecimal getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }
}
