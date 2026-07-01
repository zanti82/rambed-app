package Rambed360.dto.response;

import java.math.BigDecimal;
import java.util.List;

// Datos que el backend envia al frontend para el dashboard
public class DashboardResponse {

    // Total de facturas pendientes
    private Integer totalPendientes;

    // Total por cobrar de facturas pendientes
    private BigDecimal totalPorCobrar;

     // Total por cobrar de facturas pendientes
     private BigDecimal totalPagas;

    // Total de clientes activos
    private Integer totalClientes;

    // Total de productos con stock
    private Integer totalProductos;

    // Total stock
    private Integer totalInventario;

    // Costo total del inventario
    private BigDecimal costoTotalInventario;

    // Utilidad total de facturas pagadas
    private BigDecimal utilidadTotal;

    // Top 10 clientes por total facturado
    private List<TopClienteResponse> top20Clientes;

    // Top 10 referencias mas vendidas
    private List<TopReferenciaResponse> top20Referencias;

    public DashboardResponse() {}

    public Integer getTotalPendientes() { return totalPendientes; }
    public void setTotalPendientes(Integer totalPendientes) { this.totalPendientes = totalPendientes; }

    public BigDecimal getTotalPorCobrar() { return totalPorCobrar; }
    public void setTotalPorCobrar(BigDecimal totalPorCobrar) { this.totalPorCobrar = totalPorCobrar; }
   
    public BigDecimal getTotalPagas() { return totalPagas; }
    public void setTotalPagas(BigDecimal totalPagas) { this.totalPagas = totalPagas; }
    

    public Integer getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Integer totalClientes) { this.totalClientes = totalClientes; }

    public Integer getTotalProductos() { return totalProductos; }
    public void setTotalProductos(Integer totalProductos) { this.totalProductos = totalProductos; }

    public BigDecimal getCostoTotalInventario() { return costoTotalInventario; }
    public void setCostoTotalInventario(BigDecimal costoTotalInventario) { this.costoTotalInventario = costoTotalInventario; }

    public BigDecimal getUtilidadTotal() { return utilidadTotal; }
    public void setUtilidadTotal(BigDecimal utilidadTotal) { this.utilidadTotal = utilidadTotal; }

    public List<TopClienteResponse> getTop20Clientes() { return top20Clientes; }
    public void setTop20Clientes(List<TopClienteResponse> top20Clientes) { this.top20Clientes = top20Clientes; }

    public List<TopReferenciaResponse> getTop20Referencias() { return top20Referencias; }
    public void setTop20Referencias(List<TopReferenciaResponse> top20Referencias) { this.top20Referencias = top20Referencias; }

    public Integer getTotalInventario() {
        return totalInventario;
    }

    public void setTotalInventario(Integer totalInventario) {
        this.totalInventario = totalInventario;
    }

    
}
