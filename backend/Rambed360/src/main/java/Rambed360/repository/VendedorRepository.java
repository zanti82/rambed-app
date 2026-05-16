package Rambed360.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    List<Vendedor> findByActivo(Byte activo);

    // Busca un vendedor por nombre exacto para validar duplicados
    Optional<Vendedor> findByNombre(String nombre);

    // Busca un vendedor por identificacion para validar duplicados
    Optional<Vendedor> findByIdentificacion(String identificacion);
}
