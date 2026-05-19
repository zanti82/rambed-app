package Rambed360.repository;

import Rambed360.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su username para el login
    Optional<Usuario> findByUsername(String username);
}