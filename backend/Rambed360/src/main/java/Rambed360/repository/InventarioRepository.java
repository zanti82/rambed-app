package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Rambed360.entity.Inventario;
import Rambed360.entity.Referencia;
import Rambed360.entity.Talla;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Busca todo el stock de una referencia en todas sus tallas
    List<Inventario> findByReferencia(Referencia referencia);

    // Busca el stock de una referencia en una talla especifica
    Optional<Inventario> findByReferenciaAndTalla(Referencia referencia, Talla talla);

    // Busca todos los registros con stock mayor a cero
    List<Inventario> findByCantidadGreaterThan(Integer cantidad);

    Optional<Inventario> findByReferenciaIdAndTalla(Long referenciaId, Talla talla);
    
    // Calcula el costo total del inventario
    @Query("SELECT SUM(i.costo * i.cantidad) FROM Inventario i")
    BigDecimal calcularCostoTotalInventario();
    
}
