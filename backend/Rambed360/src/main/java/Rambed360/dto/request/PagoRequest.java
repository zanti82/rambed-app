package Rambed360.dto.request;


import java.math.BigDecimal;

public class PagoRequest {

    // Porcentaje de descuento opcional al momento del pago
    private BigDecimal descuentoPorcentaje;

    // Porcentaje de comision de pago, por defecto 4, editable
    private BigDecimal porcComisionPago;

    public BigDecimal getDescuentoPorcentaje() { return descuentoPorcentaje; }
    
    public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) 
    { this.descuentoPorcentaje = descuentoPorcentaje; }

    public BigDecimal getPorcComisionPago() { return porcComisionPago; }

    public void setPorcComisionPago(BigDecimal porcComisionPago) 
    { this.porcComisionPago = porcComisionPago; }
}
