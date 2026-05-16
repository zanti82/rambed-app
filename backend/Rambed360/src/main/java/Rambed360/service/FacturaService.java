package Rambed360.service;

import org.springframework.stereotype.Service;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

   // Agrega los dos repositorios nuevos al FacturaService
private final FacturaDetalleRepository facturaDetalleRepository;
private final InventarioRepository inventarioRepository;

// Constructor actualizado con las cinco dependencias
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

    // Retorna todas las facturas
    public List<Factura> listarTodas() {
        // Trae todos los registros de la tabla facturas
        List<Factura> todasLasFacturas = facturaRepository.findAll();
        return todasLasFacturas;
    }

    // Retorna facturas filtradas por estado
    public List<Factura> listarPorEstado(EstadoFactura estado) {
        // Busca facturas con ese estado especifico
        List<Factura> facturasPorEstado = facturaRepository.findByEstado(estado);
        return facturasPorEstado;
    }

    // Retorna facturas de un cliente especifico
    public List<Factura> listarPorCliente(Long clienteId) {
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
        return facturasPorCliente;
    }

    // Retorna facturas de un vendedor especifico
    public List<Factura> listarPorVendedor(Long vendedorId) {
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
        return facturasPorVendedor;
    }

    // Busca una factura por su ID
    public Factura buscarPorId(Long id) {
        // Intenta encontrar la factura en la base de datos
        Optional<Factura> resultado = facturaRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }

        // Guarda y retorna la factura encontrada
        Factura facturaEncontrada = resultado.get();
        return facturaEncontrada;
    }

    // Guarda una factura nueva
    public Factura guardar(Factura factura) {

        // Valida que el numero de factura no venga vacio
        if (factura.getNumeroFactura() == null || factura.getNumeroFactura().trim().isEmpty()) {
            throw new RuntimeException("El numero de factura es obligatorio");
        }

        // Valida que venga un cliente
        if (factura.getCliente() == null || factura.getCliente().getId() == null) {
            throw new RuntimeException("El cliente es obligatorio");
        }

        // Valida que venga un vendedor
        if (factura.getVendedor() == null || factura.getVendedor().getId() == null) {
            throw new RuntimeException("El vendedor es obligatorio");
        }

        // Valida que venga la fecha de emision
        if (factura.getFechaEmision() == null) {
            throw new RuntimeException("La fecha de emision es obligatoria");
        }

        // Valida que no exista otra factura con el mismo numero
        Optional<Factura> existente = facturaRepository.findByNumeroFactura(factura.getNumeroFactura().trim());

        // Si ya existe lanza un error
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe una factura con el numero: " + factura.getNumeroFactura());
        }

        // Busca el cliente en la base de datos
        Optional<Cliente> clienteResultado = clienteRepository.findById(factura.getCliente().getId());

        // Si el cliente no existe lanza un error
        if (clienteResultado.isPresent() == false) {
            throw new RuntimeException("El cliente no existe");
        }

        // Busca el vendedor en la base de datos
        Optional<Vendedor> vendedorResultado = vendedorRepository.findById(factura.getVendedor().getId());

        // Si el vendedor no existe lanza un error
        if (vendedorResultado.isPresent() == false) {
            throw new RuntimeException("El vendedor no existe");
        }

        // Asigna el cliente y vendedor completos a la factura
        factura.setCliente(clienteResultado.get());
        factura.setVendedor(vendedorResultado.get());

        // Asigna el estado pendiente por defecto
        factura.setEstado(EstadoFactura.pendiente);

        // Limpia el numero de factura
        factura.setNumeroFactura(factura.getNumeroFactura().trim());

        // Guarda y retorna la nueva factura
        Factura facturaGuardada = facturaRepository.save(factura);
        return facturaGuardada;
    }

    // Registra el pago de una factura y aplica el descuento si viene
    public Factura registrarPago(Long id, BigDecimal descuentoPorcentaje) {

        // Verifica que la factura exista
        Factura factura = buscarPorId(id);

        // Valida que la factura no este ya pagada
        if (factura.getEstado() == EstadoFactura.pagada) {
            throw new RuntimeException("La factura ya fue pagada");
        }

        // Valida que la factura no este anulada
        if (factura.getEstado() == EstadoFactura.anulada) {
            throw new RuntimeException("No se puede pagar una factura anulada");
        }

        // Si viene descuento lo aplica, si no el total es igual al subtotal
        if (descuentoPorcentaje != null && descuentoPorcentaje.compareTo(BigDecimal.ZERO) > 0) {

            // Guarda el porcentaje de descuento
            factura.setDescuentoPorcentaje(descuentoPorcentaje);

            // Calcula el valor del descuento sobre el subtotal
            BigDecimal valorDescuento = factura.getSubtotal()
                .multiply(descuentoPorcentaje)
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
        factura.setFechaPago(java.time.LocalDate.now());

        // Cambia el estado a pagada
        factura.setEstado(EstadoFactura.pagada);

        // Guarda y retorna la factura pagada
        Factura facturaPageda = facturaRepository.save(factura);
        return facturaPageda;
    }

 

    // SOLO DESARROLLO - elimina fisicamente la factura
    public void eliminar(Long id) {
        // Verifica que la factura exista antes de eliminar
        buscarPorId(id);
        facturaRepository.deleteById(id);
    }


     // Anula una factura y devuelve las unidades al inventario
    public Factura anular(Long id) {

        // Verifica que la factura exista
        Factura factura = buscarPorId(id);

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

        // Guarda y retorna la factura anulada
        Factura facturaAnulada = facturaRepository.save(factura);
        return facturaAnulada;
    }
}
