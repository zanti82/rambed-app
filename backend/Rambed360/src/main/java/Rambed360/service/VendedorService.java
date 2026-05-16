package Rambed360.service;

import java.util.List;

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

    public Vendedor guardar(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    public Vendedor buscarPorId(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
    }

    public Vendedor actualizar(Long id, Vendedor datos) {
        Vendedor vendedor = buscarPorId(id);
        vendedor.setNombre(datos.getNombre());
        vendedor.setTelefono(datos.getTelefono());
        vendedor.setCorreo(datos.getCorreo());
        vendedor.setActivo(datos.getActivo());
        return vendedorRepository.save(vendedor);
    }

    public void eliminar(Long id) {
        vendedorRepository.deleteById(id);
    }
    
}
