package Rambed360.service;

import org.springframework.stereotype.Service;

import Rambed360.entity.EstadoFactura;
import Rambed360.entity.Factura;
import Rambed360.entity.FacturaDetalle;
import Rambed360.entity.Inventario;
import Rambed360.repository.FacturaDetalleRepository;
import Rambed360.repository.FacturaRepository;
import Rambed360.repository.InventarioRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaDetalleService {

    // Repositorio para acceder a la tabla factura_detalle
    private final FacturaDetalleRepository facturaDetalleRepository;

    // Repositorio para validar que la factura exista
    private final FacturaRepository facturaRepository;

    // Repositorio para validar y actualizar el inventario
    private final InventarioRepository inventarioRepository;

    // Constructor explicito con las tres dependencias
    public FacturaDetalleService(FacturaDetalleRepository facturaDetalleRepository,
                                  FacturaRepository facturaRepository,
                                  InventarioRepository inventarioRepository) {
        this.facturaDetalleRepository = facturaDetalleRepository;
        this.facturaRepository = facturaRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // Retorna todos los items de una factura
    public List<FacturaDetalle> listarPorFactura(Long facturaId) {
        // Busca la factura en la base de datos
        Optional<Factura> resultado = facturaRepository.findById(facturaId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + facturaId);
        }

        // Guarda la factura encontrada
        Factura factura = resultado.get();

        // Busca todos los items de esa factura
        List<FacturaDetalle> itemsDeFactura = facturaDetalleRepository.findByFactura(factura);
        return itemsDeFactura;
    }

    // Agrega un item a una factura y descuenta del inventario
    public FacturaDetalle agregar(FacturaDetalle detalle) {

        // Valida que venga una factura
        if (detalle.getFactura() == null || detalle.getFactura().getId() == null) {
            throw new RuntimeException("La factura es obligatoria");
        }

        // Valida que venga un inventario
        if (detalle.getInventario() == null || detalle.getInventario().getId() == null) {
            throw new RuntimeException("El producto es obligatorio");
        }

        // Valida que la cantidad sea mayor a cero
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // Valida que el precio unitario sea mayor a cero
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio unitario debe ser mayor a cero");
        }

        // Busca la factura en la base de datos
        Optional<Factura> facturaResultado = facturaRepository.findById(detalle.getFactura().getId());

        // Si la factura no existe lanza un error
        if (facturaResultado.isPresent() == false) {
            throw new RuntimeException("La factura no existe");
        }

        // Guarda la factura encontrada
        Factura factura = facturaResultado.get();

        // Valida que la factura este en estado pendiente
        if (factura.getEstado() != EstadoFactura.pendiente) {
            throw new RuntimeException("Solo se pueden agregar items a facturas en estado pendiente");
        }

        // Busca el registro de inventario en la base de datos
        Optional<Inventario> inventarioResultado = inventarioRepository.findById(detalle.getInventario().getId());

        // Si el inventario no existe lanza un error
        if (inventarioResultado.isPresent() == false) {
            throw new RuntimeException("El producto no existe en inventario");
        }

        // Guarda el inventario encontrado
        Inventario inventario = inventarioResultado.get();

        // Valida que haya suficiente stock disponible
        if (inventario.getCantidad() < detalle.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventario.getCantidad());
        }

        // Descuenta las unidades del inventario
        Integer stockActual = inventario.getCantidad();
        Integer stockNuevo = stockActual - detalle.getCantidad();
        inventario.setCantidad(stockNuevo);
        inventarioRepository.save(inventario);

        // Asigna la factura y el inventario completos al detalle
        detalle.setFactura(factura);
        detalle.setInventario(inventario);

        // Calcula el nuevo subtotal de la factura sumando este item
        BigDecimal subtotalItem = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
        BigDecimal subtotalActual = factura.getSubtotal();
        BigDecimal subtotalNuevo = subtotalActual.add(subtotalItem);

        // Actualiza el subtotal y total de la factura
        factura.setSubtotal(subtotalNuevo);
        factura.setTotal(subtotalNuevo);
        facturaRepository.save(factura);

        // Guarda y retorna el nuevo item
        FacturaDetalle detalleGuardado = facturaDetalleRepository.save(detalle);
        return detalleGuardado;
    }

    // Elimina un item de una factura y devuelve las unidades al inventario
    public void eliminar(Long id) {

        // Busca el item en la base de datos
        Optional<FacturaDetalle> resultado = facturaDetalleRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Item de factura no encontrado con id: " + id);
        }

        // Guarda el item encontrado
        FacturaDetalle detalle = resultado.get();

        // Valida que la factura este en estado pendiente
        if (detalle.getFactura().getEstado() != EstadoFactura.pendiente) {
            throw new RuntimeException("Solo se pueden eliminar items de facturas en estado pendiente");
        }

        // Devuelve las unidades al inventario
        Inventario inventario = detalle.getInventario();
        Integer stockActual = inventario.getCantidad();
        Integer stockDevuelto = stockActual + detalle.getCantidad();
        inventario.setCantidad(stockDevuelto);
        inventarioRepository.save(inventario);

        // Resta el subtotal del item al subtotal de la factura
        Factura factura = detalle.getFactura();
        BigDecimal subtotalItem = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
        BigDecimal subtotalNuevo = factura.getSubtotal().subtract(subtotalItem);

        // Actualiza el subtotal y total de la factura
        factura.setSubtotal(subtotalNuevo);
        factura.setTotal(subtotalNuevo);
        facturaRepository.save(factura);

        // Elimina el item de la factura
        facturaDetalleRepository.deleteById(id);
    }
}
