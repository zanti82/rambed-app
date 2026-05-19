package Rambed360.service;

import Rambed360.dto.request.FacturaDetalleRequest;
import Rambed360.dto.response.FacturaDetalleResponse;
import Rambed360.entity.Factura;
import Rambed360.entity.FacturaDetalle;
import Rambed360.entity.EstadoFactura;
import Rambed360.entity.Inventario;
import Rambed360.repository.FacturaDetalleRepository;
import Rambed360.repository.FacturaRepository;
import Rambed360.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    // Retorna todos los items de una factura como DTO
    public List<FacturaDetalleResponse> listarPorFactura(Long facturaId) {
        // Busca la factura en la base de datos
        Optional<Factura> resultado = facturaRepository.findById(facturaId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + facturaId);
        }

        // Guarda la factura encontrada
        Factura factura = resultado.get();

        // Busca todos los items de esa factura
        List<FacturaDetalle> items = facturaDetalleRepository.findByFactura(factura);

        // Convierte cada item a DTO y los agrega a la lista de respuesta
        List<FacturaDetalleResponse> respuesta = new ArrayList<>();
        for (FacturaDetalle item : items) {
            FacturaDetalleResponse dto = convertirAResponse(item);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Agrega un item a una factura y descuenta del inventario
    public FacturaDetalleResponse agregar(FacturaDetalleRequest request) {

        // Valida que venga una factura
        if (request.getFacturaId() == null) {
            throw new RuntimeException("La factura es obligatoria");
        }

        // Valida que venga un inventario
        if (request.getInventarioId() == null) {
            throw new RuntimeException("El producto es obligatorio");
        }

        // Valida que la cantidad sea mayor a cero
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // Valida que el precio unitario sea mayor a cero
        if (request.getPrecioUnitario() == null || request.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio unitario debe ser mayor a cero");
        }

        // Busca la factura en la base de datos
        Optional<Factura> facturaResultado = facturaRepository.findById(request.getFacturaId());

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
        Optional<Inventario> inventarioResultado = inventarioRepository.findById(request.getInventarioId());

        // Si el inventario no existe lanza un error
        if (inventarioResultado.isPresent() == false) {
            throw new RuntimeException("El producto no existe en inventario");
        }

        // Guarda el inventario encontrado
        Inventario inventario = inventarioResultado.get();

        // Valida que haya suficiente stock disponible
        if (inventario.getCantidad() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventario.getCantidad());
        }

        // Descuenta las unidades del inventario
        Integer stockActual = inventario.getCantidad();
        Integer stockNuevo = stockActual - request.getCantidad();
        inventario.setCantidad(stockNuevo);
        inventarioRepository.save(inventario);

        // Crea el nuevo item de factura con los datos del request
        FacturaDetalle detalle = new FacturaDetalle();
        detalle.setFactura(factura);
        detalle.setInventario(inventario);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(request.getPrecioUnitario());

        // Calcula el subtotal de este item
        BigDecimal subtotalItem = request.getPrecioUnitario().multiply(new BigDecimal(request.getCantidad()));

        // Suma el subtotal del item al subtotal actual de la factura
        BigDecimal subtotalNuevo = factura.getSubtotal().add(subtotalItem);

        // Actualiza el subtotal y total de la factura
        factura.setSubtotal(subtotalNuevo);
        factura.setTotal(subtotalNuevo);
        facturaRepository.save(factura);

        // Guarda el nuevo item en la base de datos
        FacturaDetalle detalleGuardado = facturaDetalleRepository.save(detalle);

        // Convierte el item guardado a DTO y lo retorna
        FacturaDetalleResponse respuesta = convertirAResponse(detalleGuardado);
        return respuesta;
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

        // Elimina el item de la base de datos
        facturaDetalleRepository.deleteById(id);
    }

    // Convierte un Entity FacturaDetalle a DTO de respuesta
    private FacturaDetalleResponse convertirAResponse(FacturaDetalle detalle) {

        // Crea el objeto de respuesta
        FacturaDetalleResponse response = new FacturaDetalleResponse();

        // Asigna el ID del item
        response.setId(detalle.getId());

        // Asigna el ID y numero de la factura
        response.setFacturaId(detalle.getFactura().getId());
        response.setNumeroFactura(detalle.getFactura().getNumeroFactura());

        // Asigna el ID del inventario
        response.setInventarioId(detalle.getInventario().getId());

        // Asigna marca y referencia del producto
        response.setMarca(detalle.getInventario().getReferencia().getMarca());
        response.setReferencia(detalle.getInventario().getReferencia().getReferencia());

        // Asigna la talla del producto
        response.setTalla(detalle.getInventario().getTalla());

        // Asigna la cantidad vendida
        response.setCantidad(detalle.getCantidad());

        // Asigna el precio unitario
        response.setPrecioUnitario(detalle.getPrecioUnitario());

        // Calcula y asigna el subtotal de este item
        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
        response.setSubtotal(subtotal);

        return response;
    }
}