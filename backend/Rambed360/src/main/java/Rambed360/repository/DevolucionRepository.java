package Rambed360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Devolucion;
import Rambed360.entity.FacturaDetalle;

public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {

    // Busca todas las devoluciones de un item de factura especifico
    List<Devolucion> findByFacturaDetalle(FacturaDetalle facturaDetalle);
}
