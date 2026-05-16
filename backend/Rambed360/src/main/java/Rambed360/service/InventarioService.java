package Rambed360.service;

import org.springframework.stereotype.Service;

import Rambed360.entity.Inventario;
import Rambed360.entity.Referencia;
import Rambed360.entity.Talla;
import Rambed360.repository.InventarioRepository;
import Rambed360.repository.ReferenciaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    // Repositorio para acceder a la tabla inventario
    private final InventarioRepository inventarioRepository;

    // Repositorio para validar que la referencia exista
    private final ReferenciaRepository referenciaRepository;

    // Constructor explícito con las dos dependencias
    public InventarioService(InventarioRepository inventarioRepository, ReferenciaRepository referenciaRepository) {
        this.inventarioRepository = inventarioRepository;
        this.referenciaRepository = referenciaRepository;
    }

   // Retorna todo el inventario
   public List<Inventario> listarTodo() {
    // Trae todos los registros de la tabla inventario
    List<Inventario> todoElInventario = inventarioRepository.findAll();
    return todoElInventario;
}

     // Retorna el stock de una referencia en todas sus tallas
    public List<Inventario> listarPorReferencia(Long referenciaId) {
        // Busca la referencia en la base de datos
        Optional<Referencia> resultado = referenciaRepository.findById(referenciaId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Referencia no encontrada con id: " + referenciaId);
        }

        Referencia referencia = resultado.get();
        

        // Busca todo el stock de esa referencia
        List<Inventario> stockPorReferencia = inventarioRepository.findByReferencia(referencia);
        return stockPorReferencia;
    }

    // Retorna solo los registros con stock disponible
    public List<Inventario> listarConStock() {
        // Stock disponible = cantidad mayor a 0
        List<Inventario> inventarioDisponible = inventarioRepository.findByCantidadGreaterThan(0);
        return inventarioDisponible;
    }

     // Busca un registro de inventario por su ID
     public Inventario buscarPorId(Long id) {
        // Intenta encontrar el registro en la base de datos
        Optional<Inventario> resultado = inventarioRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Registro de inventario no encontrado con id: " + id);
        }

        // Guarda y retorna el registro encontrado
        Inventario inventarioEncontrado = resultado.get();
        return inventarioEncontrado;
    }

    // Guarda un registro nuevo de inventario
    public Inventario guardar(Inventario inventario) {

        // Valida que venga una referencia
        if (inventario.getReferencia() == null || inventario.getReferencia().getId() == null) {
            throw new RuntimeException("La referencia es obligatoria");
        }

        // Valida que venga una talla
        if (inventario.getTalla() == null) {
            throw new RuntimeException("La talla es obligatoria");
        }

        // Valida que la cantidad no sea negativa
        if (inventario.getCantidad() == null || inventario.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        // Valida que el precio no sea negativo
        if (inventario.getPrecio() == null || inventario.getPrecio().signum() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }

        // Busca la referencia en la base de datos
        Optional<Referencia> referenciaResultado = referenciaRepository.findById(inventario.getReferencia().getId());

        // Si la referencia no existe lanza un error
        if (referenciaResultado.isPresent() == false) {
            throw new RuntimeException("La referencia no existe");
        }

        // Guarda la referencia encontrada
        Referencia referenciaEncontrada = referenciaResultado.get();

        // Valida que no exista ya un registro con la misma referencia y talla
        Optional<Inventario> existente = inventarioRepository.findByReferenciaAndTalla(
            referenciaEncontrada,
            inventario.getTalla()
        );

        // Si ya existe lanza un error
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un registro para esa referencia y talla");
        }

        // Asigna la referencia completa al inventario
        inventario.setReferencia(referenciaEncontrada);

        // Guarda y retorna el nuevo registro
        Inventario inventarioGuardado = inventarioRepository.save(inventario);
        return inventarioGuardado;
    }

    // Actualiza la cantidad y precio de un registro de inventario
    public Inventario actualizar(Long id, Inventario datos) {

        // Verifica que el registro exista
        Inventario inventario = buscarPorId(id);

        // Valida que la cantidad no sea negativa
        if (datos.getCantidad() == null || datos.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        // Valida que el precio no sea negativo
        if (datos.getPrecio() == null || datos.getPrecio().signum() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }

        
        // Actualiza la cantidad con el nuevo valor
        inventario.setCantidad(datos.getCantidad());

        // Actualiza el precio con el nuevo valor
        inventario.setPrecio(datos.getPrecio());

        // Guarda y retorna el registro actualizado
        Inventario inventarioActualizado = inventarioRepository.save(inventario);
        return inventarioActualizado;
    }

    // SOLO DESARROLLO - elimina fisicamente el registro
    public void eliminar(Long id) {
        // Verifica que el registro exista antes de eliminar
        buscarPorId(id);
        inventarioRepository.deleteById(id);
    }

        // Busca un registro de inventario por referencia y talla
    public Inventario buscarPorReferenciaYTalla(Long referenciaId, Talla talla) {

        // Busca el registro con esa referencia y talla
        Optional<Inventario> resultado = inventarioRepository.findByReferenciaIdAndTalla(referenciaId, talla);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("No existe inventario para esa referencia y talla");
        }

        // Guarda y retorna el registro encontrado
        Inventario inventarioEncontrado = resultado.get();
        return inventarioEncontrado;
    }

    
}
