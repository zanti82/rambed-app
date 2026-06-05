package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Rambed360.entity.Comision;
import Rambed360.entity.Factura;
import Rambed360.entity.Vendedor;
import java.util.List;
import java.util.Optional;

public interface ComisionRepository extends JpaRepository<Comision, Long> {
    // Busca la comision de una factura especifica
    Optional<Comision> findByFactura(Factura factura);
    // Busca todas las comisiones de un vendedor
    List<Comision> findByVendedor(Vendedor vendedor);
    // Busca comisiones pendientes de liquidar de un vendedor
    List<Comision> findByVendedorAndLiquidada(Vendedor vendedor, Integer liquidada);
    // Busca todas las comisiones pendientes de liquidar
    List<Comision> findByLiquidada(Integer liquidada);
}