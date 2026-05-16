package Rambed360.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Cliente;
import Rambed360.entity.Vendedor;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Busca clientes por estado activo o inactivo
    List<Cliente> findByActivo(Byte activo);

    // Busca clientes por vendedor asignado
    List<Cliente> findByVendedor(Vendedor vendedor);

    // Busca clientes por ciudad
    List<Cliente> findByCiudad(String ciudad);

    // Busca un cliente por identificacion para validar duplicados
    Optional<Cliente> findByIdentificacion(String identificacion);
}
