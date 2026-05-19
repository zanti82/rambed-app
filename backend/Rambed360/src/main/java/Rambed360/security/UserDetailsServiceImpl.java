package Rambed360.security;

import Rambed360.entity.Usuario;
import Rambed360.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositorio para buscar el usuario en la base de datos
    private final UsuarioRepository usuarioRepository;

    // Constructor explicito con la dependencia del repositorio
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Carga el usuario por su username para Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Busca el usuario en la base de datos
        Optional<Usuario> resultado = usuarioRepository.findByUsername(username);

        // Si no existe lanza un error de Spring Security
        if (resultado.isPresent() == false) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Guarda el usuario encontrado
        Usuario usuario = resultado.get();

        // Crea la lista de permisos con el rol del usuario
        List<SimpleGrantedAuthority> permisos = new ArrayList<>();
        permisos.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()));

        // Retorna el usuario con su contrasena y permisos para Spring Security
        UserDetails userDetails = User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .authorities(permisos)
            .build();

        return userDetails;
    }
}