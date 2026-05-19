package Rambed360.repository;

import Rambed360.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    // Busca un rol por su nombre
    Optional<Rol> findByNombre(String nombre);
}