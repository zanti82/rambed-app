package Rambed360.dto.request;



public class DevolucionRequest {

    // ID del item de factura que se devuelve
    private Long facturaDetalleId;

    // Cantidad de unidades a devolver
    private Integer cantidad;

    // Motivo de la devolucion, opcional
    private String motivo;

    public Long getFacturaDetalleId() { return facturaDetalleId; }
    public void setFacturaDetalleId(Long facturaDetalleId) { this.facturaDetalleId = facturaDetalleId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
