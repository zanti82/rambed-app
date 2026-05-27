package Rambed360.service;

import Rambed360.dto.request.InventarioRequest;
import Rambed360.dto.response.InventarioResponse;
import Rambed360.entity.Inventario;
import Rambed360.entity.Referencia;
import Rambed360.entity.Talla;
import Rambed360.repository.InventarioRepository;
import Rambed360.repository.ReferenciaRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    // Repositorio para acceder a la tabla inventario
    private final InventarioRepository inventarioRepository;

    // Repositorio para validar que la referencia exista
    private final ReferenciaRepository referenciaRepository;

    // Constructor explicito con las dos dependencias
    public InventarioService(InventarioRepository inventarioRepository, ReferenciaRepository referenciaRepository) {
        this.inventarioRepository = inventarioRepository;
        this.referenciaRepository = referenciaRepository;
    }

    // Retorna todo el inventario como DTO
    public List<InventarioResponse> listarTodo() {
        // Trae todos los registros de la tabla inventario
        List<Inventario> todosLosRegistros = inventarioRepository.findAll();

        // Convierte cada registro a DTO y lo agrega a la lista
        List<InventarioResponse> respuesta = new ArrayList<>();
        for (Inventario inventario : todosLosRegistros) {
            InventarioResponse dto = convertirAResponse(inventario);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Retorna el stock de una referencia en todas sus tallas como DTO
    public List<InventarioResponse> listarPorReferencia(Long referenciaId) {
        // Busca la referencia en la base de datos
        Optional<Referencia> resultado = referenciaRepository.findById(referenciaId);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Referencia no encontrada con id: " + referenciaId);
        }

        // Guarda la referencia encontrada
        Referencia referencia = resultado.get();

        // Busca todo el stock de esa referencia
        List<Inventario> stockPorReferencia = inventarioRepository.findByReferencia(referencia);

        // Convierte cada registro a DTO y lo agrega a la lista
        List<InventarioResponse> respuesta = new ArrayList<>();
        for (Inventario inventario : stockPorReferencia) {
            InventarioResponse dto = convertirAResponse(inventario);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Retorna solo los registros con stock disponible como DTO
    public List<InventarioResponse> listarConStock() {
        // Busca todos los registros con cantidad mayor a 0
        List<Inventario> inventarioDisponible = inventarioRepository.findByCantidadGreaterThan(0);

        // Convierte cada registro a DTO y lo agrega a la lista
        List<InventarioResponse> respuesta = new ArrayList<>();
        for (Inventario inventario : inventarioDisponible) {
            InventarioResponse dto = convertirAResponse(inventario);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // Busca un registro de inventario por su ID y lo retorna como DTO
    public InventarioResponse buscarPorId(Long id) {
        // Intenta encontrar el registro en la base de datos
        Optional<Inventario> resultado = inventarioRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Registro de inventario no encontrado con id: " + id);
        }

        // Guarda el registro encontrado
        Inventario inventarioEncontrado = resultado.get();

        // Convierte y retorna como DTO
        InventarioResponse respuesta = convertirAResponse(inventarioEncontrado);
        return respuesta;
    }

    // Busca un registro por referencia y talla y lo retorna como DTO
    public InventarioResponse buscarPorReferenciaYTalla(Long referenciaId, Talla talla) {
        // Busca el registro con esa referencia y talla
        Optional<Inventario> resultado = inventarioRepository.findByReferenciaIdAndTalla(referenciaId, talla);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("No existe inventario para esa referencia y talla");
        }

        // Guarda el registro encontrado
        Inventario inventarioEncontrado = resultado.get();

        // Convierte y retorna como DTO
        InventarioResponse respuesta = convertirAResponse(inventarioEncontrado);
        return respuesta;
    }

    // Guarda un registro nuevo de inventario recibiendo un DTO
    public InventarioResponse guardar(InventarioRequest request) {

        // Valida que venga una referencia
        if (request.getReferenciaId() == null) {
            throw new RuntimeException("La referencia es obligatoria");
        }

        // Valida que venga una talla
        if (request.getTalla() == null) {
            throw new RuntimeException("La talla es obligatoria");
        }

        // Valida que la cantidad no sea negativa
        if (request.getCantidad() == null || request.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        // Valida que el precio no sea negativo
        if (request.getPrecio() == null || request.getPrecio().signum() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }

        // para cehquear
        System.out.println("Talla recibida: " + request.getTalla());
        System.out.println("ReferenciaId recibida: " + request.getReferenciaId());

        // Busca la referencia en la base de datos
        Optional<Referencia> referenciaResultado = referenciaRepository.findById(request.getReferenciaId());

        // Si la referencia no existe lanza un error
        if (referenciaResultado.isPresent() == false) {
            throw new RuntimeException("La referencia no existe");
        }

        // Guarda la referencia encontrada
        Referencia referenciaEncontrada = referenciaResultado.get();

        // Valida que no exista ya un registro con la misma referencia y talla
        Optional<Inventario> existente = inventarioRepository.findByReferenciaAndTalla(
            referenciaEncontrada,
            request.getTalla()
        );

           
        // Si ya existe lanza un error
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un registro para esa referencia y talla");
        }

        // Crea el nuevo registro de inventario con los datos del request
        Inventario inventario = new Inventario();
        inventario.setReferencia(referenciaEncontrada);
        inventario.setTalla(request.getTalla());
        inventario.setCantidad(request.getCantidad());
        inventario.setPrecio(request.getPrecio());

        // Guarda el registro en la base de datos
        Inventario inventarioGuardado = inventarioRepository.save(inventario);

        // Convierte y retorna como DTO
        InventarioResponse respuesta = convertirAResponse(inventarioGuardado);
        return respuesta;
    }

    // Actualiza cantidad y precio de un registro de inventario
    public InventarioResponse actualizar(Long id, InventarioRequest request) {

        // Verifica que el registro exista
        Optional<Inventario> resultado = inventarioRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Registro de inventario no encontrado con id: " + id);
        }

        // Guarda el registro encontrado
        Inventario inventario = resultado.get();

        // Valida que la cantidad no sea negativa
        if (request.getCantidad() == null || request.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        // Valida que el precio no sea negativo
        if (request.getPrecio() == null || request.getPrecio().signum() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }

        // Actualiza la cantidad con el nuevo valor
        inventario.setCantidad(request.getCantidad());

        // Actualiza el precio con el nuevo valor
        inventario.setPrecio(request.getPrecio());

        // Guarda el registro actualizado
        Inventario inventarioActualizado = inventarioRepository.save(inventario);

        // Convierte y retorna como DTO
        InventarioResponse respuesta = convertirAResponse(inventarioActualizado);
        return respuesta;
    }

    // SOLO DESARROLLO - elimina fisicamente el registro
    public void eliminar(Long id) {
        // Verifica que el registro exista antes de eliminar
        Optional<Inventario> resultado = inventarioRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Registro de inventario no encontrado con id: " + id);
        }

        inventarioRepository.deleteById(id);
    }

    // Convierte un Entity Inventario a DTO de respuesta
    private InventarioResponse convertirAResponse(Inventario inventario) {

        // Crea el objeto de respuesta
        InventarioResponse response = new InventarioResponse();

        // Asigna el ID del registro
        response.setId(inventario.getId());

        // Asigna el ID de la referencia
        response.setReferenciaId(inventario.getReferencia().getId());

        // Asigna la marca del producto
        response.setMarca(inventario.getReferencia().getMarca());

        // Asigna el nombre de la referencia
        response.setReferencia(inventario.getReferencia().getReferencia());

        // Asigna la talla
        response.setTalla(inventario.getTalla());

        // Asigna la cantidad disponible
        response.setCantidad(inventario.getCantidad());

        // Asigna el precio unitario
        response.setPrecio(inventario.getPrecio());

        return response;
    }
}