package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Rambed360.entity.Factura;
import Rambed360.entity.FacturaPago;
import java.util.List;

public interface FacturaPagoRepository extends JpaRepository<FacturaPago, Long> {
    // Busca todos los abonos de una factura
    List<FacturaPago> findByFactura(Factura factura);
}