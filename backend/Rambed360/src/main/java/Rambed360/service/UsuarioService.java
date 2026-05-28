package Rambed360.service;

import Rambed360.dto.request.CambiarPasswordRequest;
import Rambed360.dto.request.UsuarioRequest;
import Rambed360.dto.response.UsuarioResponse;
import Rambed360.entity.Rol;
import Rambed360.entity.Usuario;
import Rambed360.entity.Vendedor;
import Rambed360.repository.RolRepository;
import Rambed360.repository.UsuarioRepository;
import Rambed360.repository.VendedorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    // Repositorio para acceder a la tabla usuarios
    private final UsuarioRepository usuarioRepository;

    // Repositorio para buscar el rol
    private final RolRepository rolRepository;

    // Repositorio para buscar el vendedor
    private final VendedorRepository vendedorRepository;

    // Encriptador de contrasenas
    private final PasswordEncoder passwordEncoder;

    // Constructor explicito con las cuatro dependencias
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          VendedorRepository vendedorRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.vendedorRepository = vendedorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Retorna todos los usuarios como DTO
    public List<UsuarioResponse> listarTodos() {
        // Trae todos los usuarios de la base de datos
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Convierte cada usuario a DTO y lo agrega a la lista
        List<UsuarioResponse> respuesta = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioResponse dto = convertirAResponse(usuario);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Busca un usuario por su ID
    public UsuarioResponse buscarPorId(Long id) {
        // Intenta encontrar el usuario en la base de datos
        Optional<Usuario> resultado = usuarioRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }

        // Convierte y retorna como DTO
        UsuarioResponse respuesta = convertirAResponse(resultado.get());
        return respuesta;
    }

    // Crea un usuario nuevo
    public UsuarioResponse guardar(UsuarioRequest request) {

        // Valida que el username no venga vacio
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("El username es obligatorio");
        }

        // Valida que el password no venga vacio
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("El password es obligatorio");
        }

        // Valida que el password tenga minimo 6 caracteres
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("El password debe tener minimo 6 caracteres");
        }

        // Valida que el rol no venga vacio
        if (request.getRolId() == null) {
            throw new RuntimeException("El rol es obligatorio");
        }

        // Valida que no exista otro usuario con el mismo username
        Optional<Usuario> existente = usuarioRepository.findByUsername(request.getUsername().trim());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un usuario con el username: " + request.getUsername());
        }

        // Busca el rol en la base de datos
        Optional<Rol> rolResultado = rolRepository.findById(request.getRolId());
        if (rolResultado.isPresent() == false) {
            throw new RuntimeException("El rol no existe");
        }

        // Guarda el rol encontrado
        Rol rol = rolResultado.get();

        // Si el rol es VENDEDOR valida que venga el vendedorId
        if (rol.getNombre().equals("VENDEDOR")) {
            if (request.getVendedorId() == null) {
                throw new RuntimeException("El vendedor es obligatorio para el rol VENDEDOR");
            }
        }

        // Crea el objeto usuario vacio
        Usuario usuarioNuevo = new Usuario();

        // Asigna el username limpio
        usuarioNuevo.setUsername(request.getUsername().trim());

        // Encripta el password antes de guardarlo
        usuarioNuevo.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asigna el rol
        usuarioNuevo.setRol(rol);

        // Asigna activo por defecto
        usuarioNuevo.setActivo((byte) 1);

        // Si viene vendedorId lo busca y asigna
        if (request.getVendedorId() != null) {
            Optional<Vendedor> vendedorResultado = vendedorRepository.findById(request.getVendedorId());
            if (vendedorResultado.isPresent() == false) {
                throw new RuntimeException("El vendedor no existe");
            }
            usuarioNuevo.setVendedor(vendedorResultado.get());
        }

        // Guarda el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuarioNuevo);

        // Convierte y retorna como DTO
        UsuarioResponse respuesta = convertirAResponse(usuarioGuardado);
        return respuesta;
    }

    // Cambia el password de un usuario
    public UsuarioResponse cambiarPassword(Long id, CambiarPasswordRequest request) {

        // Busca el usuario en la base de datos
        Optional<Usuario> resultado = usuarioRepository.findById(id);
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }

        // Guarda el usuario encontrado
        Usuario usuario = resultado.get();

        // Valida que el nuevo password no venga vacio
        if (request.getNuevaPassword() == null || request.getNuevaPassword().trim().isEmpty()) {
            throw new RuntimeException("El nuevo password es obligatorio");
        }

        // Valida que el password tenga minimo 6 caracteres
        if (request.getNuevaPassword().length() < 6) {
            throw new RuntimeException("El password debe tener minimo 6 caracteres");
        }

        // Encripta y asigna el nuevo password
        usuario.setPassword(passwordEncoder.encode(request.getNuevaPassword()));

        // Guarda el usuario actualizado
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        // Convierte y retorna como DTO
        UsuarioResponse respuesta = convertirAResponse(usuarioActualizado);
        return respuesta;
    }

    // Activa un usuario
    public UsuarioResponse activar(Long id) {
        // Busca el usuario en la base de datos
        Optional<Usuario> resultado = usuarioRepository.findById(id);
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }

        // Guarda el usuario encontrado
        Usuario usuario = resultado.get();

        // Cambia el estado a activo
        usuario.setActivo((byte) 1);

        // Guarda y retorna como DTO
        Usuario usuarioActivado = usuarioRepository.save(usuario);
        UsuarioResponse respuesta = convertirAResponse(usuarioActivado);
        return respuesta;
    }

    // Desactiva un usuario
    public UsuarioResponse desactivar(Long id) {
        // Busca el usuario en la base de datos
        Optional<Usuario> resultado = usuarioRepository.findById(id);
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }

        // Guarda el usuario encontrado
        Usuario usuario = resultado.get();

        // Cambia el estado a inactivo
        usuario.setActivo((byte) 0);

        // Guarda y retorna como DTO
        Usuario usuarioDesactivado = usuarioRepository.save(usuario);
        UsuarioResponse respuesta = convertirAResponse(usuarioDesactivado);
        return respuesta;
    }

    // Convierte un Entity Usuario a DTO de respuesta
    private UsuarioResponse convertirAResponse(Usuario usuario) {

        // Crea el objeto de respuesta vacio
        UsuarioResponse response = new UsuarioResponse();

        // Asigna el ID
        response.setId(usuario.getId());

        // Asigna el username
        response.setUsername(usuario.getUsername());

        // Asigna el nombre del rol
        response.setRol(usuario.getRol().getNombre());

        // Asigna el estado
        response.setActivo(usuario.getActivo());

        // Asigna el vendedor si existe
        if (usuario.getVendedor() != null) {
            response.setVendedorId(usuario.getVendedor().getId());
            response.setVendedorNombre(usuario.getVendedor().getNombre());
        }

        return response;
    }
}