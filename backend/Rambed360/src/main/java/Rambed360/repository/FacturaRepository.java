package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Cliente;
import Rambed360.entity.EstadoFactura;
import Rambed360.entity.Factura;
import Rambed360.entity.Vendedor;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Busca facturas por estado
    List<Factura> findByEstado(EstadoFactura estado);

    // Busca facturas por cliente
    List<Factura> findByCliente(Cliente cliente);

    // Busca facturas por vendedor
    List<Factura> findByVendedor(Vendedor vendedor);

    // Busca una factura por numero de factura para validar duplicados
    Optional<Factura> findByNumeroFactura(String numeroFactura);
}