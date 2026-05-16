package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Inventario;
import Rambed360.entity.Referencia;
import Rambed360.entity.Talla;

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
    
}
