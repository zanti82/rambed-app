package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Factura;
import Rambed360.entity.FacturaDetalle;

import java.util.List;

public interface FacturaDetalleRepository extends JpaRepository<FacturaDetalle, Long> {

    // Busca todos los items de una factura especifica
    List<FacturaDetalle> findByFactura(Factura factura);
}