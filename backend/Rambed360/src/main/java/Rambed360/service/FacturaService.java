package Rambed360.service;

import Rambed360.dto.request.FacturaRequest;
import Rambed360.dto.request.PagoRequest;
import Rambed360.dto.response.FacturaResponse;
import Rambed360.entity.Cliente;
import Rambed360.entity.EstadoFactura;
import Rambed360.entity.Factura;
import Rambed360.entity.FacturaDetalle;
import Rambed360.entity.Inventario;
import Rambed360.entity.Vendedor;
import Rambed360.repository.ClienteRepository;
import Rambed360.repository.FacturaDetalleRepository;
import Rambed360.repository.FacturaRepository;
import Rambed360.repository.InventarioRepository;
import Rambed360.repository.VendedorRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    // Repositorio para acceder a la tabla facturas
    private final FacturaRepository facturaRepository;

    // Repositorio para validar que el cliente exista
    private final ClienteRepository clienteRepository;

    // Repositorio para validar que el vendedor exista
    private final VendedorRepository vendedorRepository;

    // Repositorio para obtener los items al anular
    private final FacturaDetalleRepository facturaDetalleRepository;

    // Repositorio para devolver stock al anular
    private final InventarioRepository inventarioRepository;

    // Constructor explicito con las cinco dependencias
    public FacturaService(FacturaRepository facturaRepository,
                          ClienteRepository clienteRepository,
                          VendedorRepository vendedorRepository,
                          FacturaDetalleRepository facturaDetalleRepository,
                          InventarioRepository inventarioRepository) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.vendedorRepository = vendedorRepository;
        this.facturaDetalleRepository = facturaDetalleRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // Retorna todas las facturas como DTO
    public List<FacturaResponse> listarTodas() {
        // Trae todos los registros de la tabla facturas
        List<Factura> todasLasFacturas = facturaRepository.findAll();

        // Convierte cada factura a DTO y la agrega a la lista
        List<FacturaResponse> respuesta = new ArrayList<>();
        for (Factura factura : todasLasFacturas) {
            FacturaResponse dto = convertirAResponse(factura);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Retorna facturas filtradas por estado como DTO
    public List<FacturaResponse> listarPorEstado(EstadoFactura estado) {
        // Busca facturas con ese estado especifico
        List<Factura> facturasPorEstado = facturaRepository.findByEstado(estado);

        // Convierte cada factura a DTO y la agrega a la lista
        List<FacturaResponse> respuesta = new ArrayList<>();
        for (Factura factura : facturasPorEstado) {
            FacturaResponse dto = convertirAResponse(factura);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Retorna facturas de un cliente especifico como DTO
    public List<FacturaResponse> listarPorCliente(Long clienteId) {
        // Busca el cliente en la base de datos
        Optional<Cliente> resultado = clienteRepository.findById(clienteId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Cliente no encontrado con id: " + clienteId);
        }

        // Guarda el cliente encontrado
        Cliente cliente = resultado.get();

        // Busca todas las facturas de ese cliente
        List<Factura> facturasPorCliente = facturaRepository.findByCliente(cliente);

        // Convierte cada factura a DTO y la agrega a la lista
        List<FacturaResponse> respuesta = new ArrayList<>();
        for (Factura factura : facturasPorCliente) {
            FacturaResponse dto = convertirAResponse(factura);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Retorna facturas de un vendedor especifico como DTO
    public List<FacturaResponse> listarPorVendedor(Long vendedorId) {
        // Busca el vendedor en la base de datos
        Optional<Vendedor> resultado = vendedorRepository.findById(vendedorId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Vendedor no encontrado con id: " + vendedorId);
        }

        // Guarda el vendedor encontrado
        Vendedor vendedor = resultado.get();

        // Busca todas las facturas de ese vendedor
        List<Factura> facturasPorVendedor = facturaRepository.findByVendedor(vendedor);

        // Convierte cada factura a DTO y la agrega a la lista
        List<FacturaResponse> respuesta = new ArrayList<>();
        for (Factura factura : facturasPorVendedor) {
            FacturaResponse dto = convertirAResponse(factura);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Busca una factura por su ID y la retorna como DTO
    public FacturaResponse buscarPorId(Long id) {
        // Intenta encontrar la factura en la base de datos
        Optional<Factura> resultado = facturaRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }

        // Guarda la factura encontrada
        Factura facturaEncontrada = resultado.get();

        // Convierte y retorna como DTO
        FacturaResponse respuesta = convertirAResponse(facturaEncontrada);
        return respuesta;
    }

    // Guarda una factura nueva recibiendo un DTO
    public FacturaResponse guardar(FacturaRequest request) {

        
        // Valida que venga un cliente
        if (request.getClienteId() == null) {
            throw new RuntimeException("El cliente es obligatorio");
        }

        // Valida que venga un vendedor
        if (request.getVendedorId() == null) {
            throw new RuntimeException("El vendedor es obligatorio");
        }

        // Valida que venga la fecha de emision
        if (request.getFechaEmision() == null) {
            throw new RuntimeException("La fecha de emision es obligatoria");
        }

        
        // Busca el cliente en la base de datos
        Optional<Cliente> clienteResultado = clienteRepository.findById(request.getClienteId());

        // Si el cliente no existe lanza un error
        if (clienteResultado.isPresent() == false) {
            throw new RuntimeException("El cliente no existe");
        }

        // Busca el vendedor en la base de datos
        Optional<Vendedor> vendedorResultado = vendedorRepository.findById(request.getVendedorId());

        // Si el vendedor no existe lanza un error
        if (vendedorResultado.isPresent() == false) {
            throw new RuntimeException("El vendedor no existe");
        }

        // Genera el numero de factura automaticamente
        String numeroFactura = generarNumeroFactura();

        // Crea el objeto Factura vacio
        Factura facturaNueva = new Factura();

        // Asigna el numero generado automaticamente
        facturaNueva.setNumeroFactura(numeroFactura);

        // Asigna el cliente encontrado
        facturaNueva.setCliente(clienteResultado.get());

        // Asigna el vendedor encontrado
        facturaNueva.setVendedor(vendedorResultado.get());

        // Asigna la fecha de emision
        facturaNueva.setFechaEmision(request.getFechaEmision());

        // Asigna las notas opcionales
        facturaNueva.setNotas(request.getNotas());

        // Asigna el estado pendiente por defecto
        facturaNueva.setEstado(EstadoFactura.pendiente);

        // Guarda la factura en la base de datos
        Factura facturaGuardada = facturaRepository.save(facturaNueva);

        // Convierte y retorna como DTO
        FacturaResponse respuesta = convertirAResponse(facturaGuardada);
        return respuesta;
    }

    // Registra el pago de una factura y aplica descuento si viene
    public FacturaResponse registrarPago(Long id, PagoRequest request) {

        // Verifica que la factura exista
        Optional<Factura> resultado = facturaRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }

        // Guarda la factura encontrada
        Factura factura = resultado.get();

        // Valida que la factura no este ya pagada
        if (factura.getEstado() == EstadoFactura.pagada) {
            throw new RuntimeException("La factura ya fue pagada");
        }

        // Valida que la factura no este anulada
        if (factura.getEstado() == EstadoFactura.anulada) {
            throw new RuntimeException("No se puede pagar una factura anulada");
        }

        // Si viene descuento lo aplica, si no el total es igual al subtotal
        if (request.getDescuentoPorcentaje() != null && request.getDescuentoPorcentaje().compareTo(BigDecimal.ZERO) > 0) {

            // Guarda el porcentaje de descuento
            factura.setDescuentoPorcentaje(request.getDescuentoPorcentaje());

            // Calcula el valor del descuento sobre el subtotal
            BigDecimal valorDescuento = factura.getSubtotal()
                .multiply(request.getDescuentoPorcentaje())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            // Calcula el total restando el descuento al subtotal
            BigDecimal totalConDescuento = factura.getSubtotal().subtract(valorDescuento);

            // Asigna el total calculado
            factura.setTotal(totalConDescuento);

        } else {
            // Sin descuento el total es igual al subtotal
            factura.setTotal(factura.getSubtotal());
        }

        // Registra la fecha de pago con la fecha actual
        factura.setFechaPago(LocalDate.now());

        // Cambia el estado a pagada
        factura.setEstado(EstadoFactura.pagada);

        // Guarda la factura actualizada
        Factura facturaPagada = facturaRepository.save(factura);

        // Convierte y retorna como DTO
        FacturaResponse respuesta = convertirAResponse(facturaPagada);
        return respuesta;
    }

    // Anula una factura y devuelve las unidades al inventario
    public FacturaResponse anular(Long id) {

        // Verifica que la factura exista
        Optional<Factura> resultado = facturaRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }

        // Guarda la factura encontrada
        Factura factura = resultado.get();

        // Valida que la factura no este ya anulada
        if (factura.getEstado() == EstadoFactura.anulada) {
            throw new RuntimeException("La factura ya esta anulada");
        }

        // Busca todos los items de la factura
        List<FacturaDetalle> items = facturaDetalleRepository.findByFactura(factura);

        // Recorre cada item y devuelve las unidades al inventario
        for (FacturaDetalle item : items) {

            // Guarda el inventario del item actual
            Inventario inventario = item.getInventario();

            // Calcula el nuevo stock sumando las unidades del item
            Integer stockActual = inventario.getCantidad();
            Integer stockDevuelto = stockActual + item.getCantidad();

            // Actualiza el stock en el inventario
            inventario.setCantidad(stockDevuelto);
            inventarioRepository.save(inventario);
        }

        // Cambia el estado de la factura a anulada
        factura.setEstado(EstadoFactura.anulada);

        // Guarda la factura anulada
        Factura facturaAnulada = facturaRepository.save(factura);

        // Convierte y retorna como DTO
        FacturaResponse respuesta = convertirAResponse(facturaAnulada);
        return respuesta;
    }

        // Genera el siguiente numero de factura automaticamente
    private String generarNumeroFactura() {
        // Busca la ultima factura en la base de datos
        Optional<Factura> ultimaFactura = facturaRepository.findUltimaFactura();

        // Si no hay facturas empieza en REM-0001
        if (ultimaFactura.isPresent() == false) {
            return "REM-0001";
        }

        // Obtiene el numero de la ultima factura
        String ultimoNumero = ultimaFactura.get().getNumeroFactura();

        // Extrae solo el numero quitando el prefijo REM-
        String numeroStr = ultimoNumero.replace("REM-", "");

        // Convierte el numero a entero y suma 1
        int siguienteNumero = Integer.parseInt(numeroStr) + 1;

        // Formatea el numero con ceros a la izquierda hasta 4 digitos
        String numeroFormateado = String.format("%04d", siguienteNumero);

        return "REM-" + numeroFormateado;
    }

    // SOLO DESARROLLO - elimina fisicamente la factura
    public void eliminar(Long id) {
        // Verifica que la factura exista antes de eliminar
        Optional<Factura> resultado = facturaRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }

        facturaRepository.deleteById(id);
    }

    // Convierte un Entity Factura a DTO de respuesta
    private FacturaResponse convertirAResponse(Factura factura) {

        // Crea el objeto de respuesta vacio
        FacturaResponse response = new FacturaResponse();

        // Asigna el ID de la factura
        response.setId(factura.getId());

        // Asigna el numero de factura
        response.setNumeroFactura(factura.getNumeroFactura());

        // Asigna el ID y nombre del cliente
        response.setClienteId(factura.getCliente().getId());
        response.setClienteNombre(factura.getCliente().getNombre());
        response.setClienteTelefono(factura.getCliente().getTelefono());
        response.setClienteDireccion(factura.getCliente().getDireccion());

       
        // Asigna el ID y nombre del vendedor
        response.setVendedorId(factura.getVendedor().getId());
        response.setVendedorNombre(factura.getVendedor().getNombre());

        // Asigna la fecha de emision
        response.setFechaEmision(factura.getFechaEmision());

        // Asigna la fecha de pago
        response.setFechaPago(factura.getFechaPago());

        // Asigna el porcentaje de descuento
        response.setDescuentoPorcentaje(factura.getDescuentoPorcentaje());

        // Asigna el subtotal
        response.setSubtotal(factura.getSubtotal());

        // Asigna el total
        response.setTotal(factura.getTotal());

        // Asigna el estado
        response.setEstado(factura.getEstado());

        // Asigna las notas
        response.setNotas(factura.getNotas());

        return response;
    }
}