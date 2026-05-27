package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    // Busca la ultima factura creada para generar el siguiente numero
    @Query("SELECT f FROM Factura f ORDER BY f.id DESC LIMIT 1")
    Optional<Factura> findUltimaFactura();
}