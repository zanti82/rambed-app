package Rambed360.service;

import Rambed360.dto.response.DashboardResponse;
import Rambed360.dto.response.TopClienteResponse;
import Rambed360.dto.response.TopReferenciaResponse;
import Rambed360.entity.Cliente;
import Rambed360.entity.EstadoFactura;
import Rambed360.entity.Factura;
import Rambed360.entity.Inventario;
import Rambed360.repository.ClienteRepository;
import Rambed360.repository.FacturaRepository;
import Rambed360.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class DashboardService {

    // Repositorio para acceder a facturas
    private final FacturaRepository facturaRepository;

    // Repositorio para acceder a clientes
    private final ClienteRepository clienteRepository;

    // Repositorio para acceder a inventario
    private final InventarioRepository inventarioRepository;

    // Constructor explicito con las tres dependencias
    public DashboardService(FacturaRepository facturaRepository,
                            ClienteRepository clienteRepository,
                            InventarioRepository inventarioRepository) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // Retorna todos los datos del dashboard para el admin
    public DashboardResponse obtenerDashboardAdmin() {

        // Crea el objeto de respuesta
        DashboardResponse response = new DashboardResponse();

        // Obtiene todas las facturas pendientes
        List <Factura> facturasPendientes = facturaRepository.findByEstado(EstadoFactura.pendiente);
        response.setTotalPendientes(facturasPendientes.size());

             

        // Calcula el total por cobrar sumando los subtotales de facturas pendientes
        BigDecimal totalPorCobrar = BigDecimal.ZERO;
        for (int i = 0; i < facturasPendientes.size(); i++) {
            Factura factura = facturasPendientes.get(i);
            totalPorCobrar = totalPorCobrar.add(factura.getSubtotal());
        }
        response.setTotalPorCobrar(totalPorCobrar);

        
        // Cuenta los clientes activos
        List <Cliente>clientesActivos = clienteRepository.findByActivo((byte) 1);
        response.setTotalClientes(clientesActivos.size());

        // Cuenta los productos con stock disponible
        List<Inventario> productosConStock = inventarioRepository.findByCantidadGreaterThan(0);
        response.setTotalProductos(productosConStock.size());

        // Calcula el costo total del inventario
        BigDecimal costoTotal = inventarioRepository.calcularCostoTotalInventario();
        if (costoTotal == null) {
            costoTotal = BigDecimal.ZERO;
        }
        response.setCostoTotalInventario(costoTotal);

        // ahora - utilidad real = precio vendido - costo del producto
        BigDecimal utilidadTotal = facturaRepository.calcularUtilidadReal();

        // si no hay facturas pagadas el query retorna null
        if (utilidadTotal == null) {
            utilidadTotal = BigDecimal.ZERO;
        }
        response.setUtilidadTotal(utilidadTotal);

        // Obtiene el top 20 de clientes
        List<Object[]> rawClientes = facturaRepository.findTop10Clientes();
        List<TopClienteResponse> top20Clientes = new ArrayList<>();
        for (int i = 0; i < rawClientes.size(); i++) {
            Object[] fila = rawClientes.get(i);

            // Crea el objeto TopClienteResponse con los datos de la fila
            TopClienteResponse topCliente = new TopClienteResponse(
                (Long) fila[0],
                (String) fila[1],
                (String) fila[2],
                (BigDecimal) fila[3]
            );
            top20Clientes.add(topCliente);
        }
        response.setTop20Clientes(top20Clientes);

        // Obtiene el top 10 de referencias
        List<Object[]> rawReferencias = facturaRepository.findTop10Referencias();
        List<TopReferenciaResponse> top20Referencias = new ArrayList<>();
        for (int i = 0; i < rawReferencias.size(); i++) {
            Object[] fila = rawReferencias.get(i);

            // Crea el objeto TopReferenciaResponse con los datos de la fila
            TopReferenciaResponse topReferencia = new TopReferenciaResponse(
                (String) fila[0],
                (String) fila[1],
                (Long) fila[2]
            );
            top20Referencias.add(topReferencia);
        }
        response.setTop20Referencias(top20Referencias);

        return response;
    }

    // Retorna los datos del dashboard para un vendedor especifico
    public DashboardResponse obtenerDashboardVendedor(Long vendedorId) {

        // Crea el objeto de respuesta
        DashboardResponse response = new DashboardResponse();

        // Obtiene las facturas pendientes del vendedor
        List <Factura> todasLasFacturas = facturaRepository.findAll();
        List <Factura> facturasPendientesVendedor = new ArrayList<>();
        List <Factura> facturasPagadasVendedor = new ArrayList<>();

        // Filtra las facturas del vendedor
        for (int i = 0; i < todasLasFacturas.size(); i++) {
           Factura factura = (Factura) todasLasFacturas.get(i);

            // Verifica si la factura pertenece al vendedor
            if (factura.getVendedor().getId().equals(vendedorId)) {

                // Separa por estado
                if (factura.getEstado() == EstadoFactura.pendiente) {
                    facturasPendientesVendedor.add(factura);
                }
                if (factura.getEstado() == EstadoFactura.pagada) {
                    facturasPagadasVendedor.add(factura);
                }
            }
        }

        // Asigna el total de pendientes
        response.setTotalPendientes(facturasPendientesVendedor.size());

        // Calcula el total por cobrar del vendedor
        BigDecimal totalPorCobrar = BigDecimal.ZERO;
        for (int i = 0; i < facturasPendientesVendedor.size(); i++) {
            Factura factura = (Factura) facturasPendientesVendedor.get(i);
            totalPorCobrar = totalPorCobrar.add(factura.getSubtotal());
        }
        response.setTotalPorCobrar(totalPorCobrar);

        // El vendedor no ve costo ni utilidad
        response.setCostoTotalInventario(null);
        response.setUtilidadTotal(null);
        response.setTop20Clientes(null);
        response.setTop20Referencias(null);

        return response;
    }
}
