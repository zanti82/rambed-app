package Rambed360.service;

import Rambed360.dto.request.DevolucionRequest;
import Rambed360.dto.response.DevolucionResponse;
import Rambed360.entity.Devolucion;
import Rambed360.entity.FacturaDetalle;
import Rambed360.entity.Inventario;
import Rambed360.repository.DevolucionRepository;
import Rambed360.repository.FacturaDetalleRepository;
import Rambed360.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DevolucionService {

    // Repositorio para acceder a la tabla devoluciones
    private final DevolucionRepository devolucionRepository;

    // Repositorio para buscar el item de la factura
    private final FacturaDetalleRepository facturaDetalleRepository;

    // Repositorio para actualizar el stock del inventario
    private final InventarioRepository inventarioRepository;

    // Constructor explicito con las tres dependencias
    public DevolucionService(DevolucionRepository devolucionRepository,
                             FacturaDetalleRepository facturaDetalleRepository,
                             InventarioRepository inventarioRepository) {
        this.devolucionRepository = devolucionRepository;
        this.facturaDetalleRepository = facturaDetalleRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // Retorna todas las devoluciones como DTO
    public List<DevolucionResponse> listarTodas() {
        // Trae todos los registros de la tabla devoluciones
        List<Devolucion> devoluciones = devolucionRepository.findAll();

        // Convierte cada devolucion a DTO y la agrega a la lista
        List<DevolucionResponse> respuesta = new ArrayList<>();
        for (Devolucion devolucion : devoluciones) {
            DevolucionResponse dto = convertirAResponse(devolucion);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Retorna las devoluciones de un item de factura especifico como DTO
    public List<DevolucionResponse> listarPorFacturaDetalle(Long facturaDetalleId) {
        // Busca el item de la factura en la base de datos
        Optional<FacturaDetalle> resultado = facturaDetalleRepository.findById(facturaDetalleId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Item de factura no encontrado con id: " + facturaDetalleId);
        }

        // Guarda el item encontrado
        FacturaDetalle facturaDetalle = resultado.get();

        // Busca todas las devoluciones de ese item
        List<Devolucion> devoluciones = devolucionRepository.findByFacturaDetalle(facturaDetalle);

        // Convierte cada devolucion a DTO y la agrega a la lista
        List<DevolucionResponse> respuesta = new ArrayList<>();
        for (Devolucion devolucion : devoluciones) {
            DevolucionResponse dto = convertirAResponse(devolucion);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Busca una devolucion por su ID y la retorna como DTO
    public DevolucionResponse buscarPorId(Long id) {
        // Intenta encontrar la devolucion en la base de datos
        Optional<Devolucion> resultado = devolucionRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Devolucion no encontrada con id: " + id);
        }

        // Guarda la devolucion encontrada
        Devolucion devolucionEncontrada = resultado.get();

        // Convierte y retorna como DTO
        DevolucionResponse respuesta = convertirAResponse(devolucionEncontrada);
        return respuesta;
    }

    // Registra una devolucion y devuelve las unidades al inventario
    public DevolucionResponse registrar(DevolucionRequest request) {

        // Valida que venga un item de factura
        if (request.getFacturaDetalleId() == null) {
            throw new RuntimeException("El item de factura es obligatorio");
        }

        // Valida que la cantidad sea mayor a cero
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // Busca el item de la factura en la base de datos
        Optional<FacturaDetalle> facturaDetalleResultado = facturaDetalleRepository.findById(request.getFacturaDetalleId());

        // Si no existe lanza un error
        if (facturaDetalleResultado.isPresent() == false) {
            throw new RuntimeException("El item de factura no existe");
        }

        // Guarda el item encontrado
        FacturaDetalle facturaDetalle = facturaDetalleResultado.get();

        // Calcula el total de unidades ya devueltas de este item
        List<Devolucion> devolucionesAnteriores = devolucionRepository.findByFacturaDetalle(facturaDetalle);
        Integer totalDevueltoAnterior = 0;
        for (Devolucion devolucionAnterior : devolucionesAnteriores) {
            totalDevueltoAnterior = totalDevueltoAnterior + devolucionAnterior.getCantidad();
        }

        // Calcula cuantas unidades quedan disponibles para devolver
        Integer unidadesDisponibles = facturaDetalle.getCantidad() - totalDevueltoAnterior;

        // Valida que no se devuelvan mas unidades de las vendidas
        if (request.getCantidad() > unidadesDisponibles) {
            throw new RuntimeException("No puede devolver mas de " + unidadesDisponibles + " unidades");
        }

        // Busca el inventario del item
        Inventario inventario = facturaDetalle.getInventario();

        // Calcula el nuevo stock sumando las unidades devueltas
        Integer stockActual = inventario.getCantidad();
        Integer stockNuevo = stockActual + request.getCantidad();

        // Actualiza el stock en el inventario
        inventario.setCantidad(stockNuevo);
        inventarioRepository.save(inventario);

        // Crea la devolucion con los datos del request
        Devolucion devolucion = new Devolucion();
        devolucion.setFacturaDetalle(facturaDetalle);
        devolucion.setCantidad(request.getCantidad());
        devolucion.setMotivo(request.getMotivo());

        // Guarda la devolucion en la base de datos
        Devolucion devolucionRegistrada = devolucionRepository.save(devolucion);

        // Convierte y retorna como DTO
        DevolucionResponse respuesta = convertirAResponse(devolucionRegistrada);
        return respuesta;
    }

    // Convierte un Entity Devolucion a DTO de respuesta
    private DevolucionResponse convertirAResponse(Devolucion devolucion) {

        // Crea el objeto de respuesta
        DevolucionResponse response = new DevolucionResponse();

        // Asigna el ID de la devolucion
        response.setId(devolucion.getId());

        // Asigna el ID del item de factura
        response.setFacturaDetalleId(devolucion.getFacturaDetalle().getId());

        // Asigna el numero de factura para referencia rapida
        response.setNumeroFactura(devolucion.getFacturaDetalle().getFactura().getNumeroFactura());

        // Asigna la marca del producto devuelto
        response.setMarca(devolucion.getFacturaDetalle().getInventario().getReferencia().getMarca());

        // Asigna la referencia del producto devuelto
        response.setReferencia(devolucion.getFacturaDetalle().getInventario().getReferencia().getReferencia());

        // Asigna la talla del producto devuelto
        response.setTalla(devolucion.getFacturaDetalle().getInventario().getTalla());

        // Asigna la cantidad devuelta
        response.setCantidad(devolucion.getCantidad());

        // Asigna el motivo de la devolucion
        response.setMotivo(devolucion.getMotivo());

        // Asigna la fecha de creacion
        response.setCreadoEn(devolucion.getCreadoEn());

        return response;
    }
}