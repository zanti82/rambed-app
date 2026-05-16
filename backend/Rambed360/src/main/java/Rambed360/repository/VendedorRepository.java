package Rambed360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    List<Vendedor> findByActivo(Byte activo);
}
