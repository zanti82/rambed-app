package Rambed360.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import Rambed360.entity.Vendedor;
import Rambed360.repository.VendedorRepository;

@Service
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    
    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    public List<Vendedor> listarTodos() {
        return vendedorRepository.findAll();
    }

    public List<Vendedor> listarActivos() {
        return vendedorRepository.findByActivo((byte) 1);
    }

    public Vendedor buscarPorId(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con id: " + id));
    }

    public Vendedor guardar(Vendedor vendedor) {
       // Valida que el nombre no venga vacío
       if (vendedor.getNombre() == null || vendedor.getNombre().trim().isEmpty()) {
        throw new RuntimeException("El nombre del vendedor es obligatorio");
        }

        // Valida que no exista otro vendedor con el mismo nombre
        Optional<Vendedor> existente = vendedorRepository.findByNombre(vendedor.getNombre().trim());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un vendedor con el nombre: " + vendedor.getNombre());
        }

        // Valida que la identificacion no venga vacia
        if (vendedor.getIdentificacion() == null || vendedor.getIdentificacion().trim().isEmpty()) {
            throw new RuntimeException("La identificacion del vendedor es obligatoria");
        }

        // Asigna activo por defecto en 1 si no viene en la petición
        if (vendedor.getActivo() == null) {
            vendedor.setActivo((byte) 1);
        }

        return vendedorRepository.save(vendedor);
    }
    
   

   // Actualiza los datos de un vendedor existente
   public Vendedor actualizar(Long id, Vendedor datos) {

        // Verifica que el vendedor exista
        Vendedor vendedor = buscarPorId(id);

        // Valida que el nombre no venga vacío
        if (datos.getNombre() == null || datos.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del vendedor es obligatorio");
        }

        // Valida que el nuevo nombre no lo tenga otro vendedor diferente
        Optional<Vendedor> existente = vendedorRepository.findByNombre(datos.getNombre().trim());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe otro vendedor con el nombre: " + datos.getNombre());
        }

            // Valida que la identificacion no venga vacia
        if (datos.getIdentificacion() == null || datos.getIdentificacion().trim().isEmpty()) {
            throw new RuntimeException("La identificacion del vendedor es obligatoria");
        }

        // Actualiza los campos
        vendedor.setNombre(datos.getNombre().trim());
        vendedor.setTelefono(datos.getTelefono());
        vendedor.setCorreo(datos.getCorreo());

        return vendedorRepository.save(vendedor);
    }

    // Cambia el estado del vendedor a activo
    public Vendedor activar(Long id) {
        Vendedor vendedor = buscarPorId(id);
        vendedor.setActivo((byte) 1);
        return vendedorRepository.save(vendedor);
    }

    // Cambia el estado del vendedor a inactivo
    public Vendedor desactivar(Long id) {
        Vendedor vendedor = buscarPorId(id);
        vendedor.setActivo((byte) 0);
        return vendedorRepository.save(vendedor);
    }

    // SOLO DESARROLLO - elimina físicamente el registro
    public void eliminar(Long id) {
        buscarPorId(id);
        vendedorRepository.deleteById(id);
    }
    
}
