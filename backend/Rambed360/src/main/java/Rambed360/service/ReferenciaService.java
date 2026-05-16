package Rambed360.service;
import org.springframework.stereotype.Service;

import Rambed360.entity.Referencia;
import Rambed360.repository.ReferenciaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReferenciaService {

     // Repositorio para acceder a la base de datos
    private final ReferenciaRepository referenciaRepository;

    public ReferenciaService(ReferenciaRepository referenciaRepository) {
        this.referenciaRepository = referenciaRepository;
    }

    // Retorna todas las referencias
    public List<Referencia> listarTodas() {
        return referenciaRepository.findAll();
    }

    // Retorna todas las referencias de una marca
    public List<Referencia> listarPorMarca(String marca) {
        return referenciaRepository.findByMarca(marca);
    }

    // Busca una referencia por su ID y lanza error si no existe
    public Referencia buscarPorId(Long id) {
        // Intenta encontrar la referencia en la base de datos
        Optional<Referencia> resultado = referenciaRepository.findById(id);

        // Si no existe lanza un error
        if (resultado.isPresent() == false) {
            throw new RuntimeException("Referencia no encontrada con id: " + id);
        }

        // Retorna la referencia encontrada
        return resultado.get();
    }

    // Guarda una referencia nueva validando que no haya duplicados
    public Referencia guardar(Referencia referencia) {

        // Valida que la marca no venga vacia
        if (referencia.getMarca() == null || referencia.getMarca().trim().isEmpty()) {
            throw new RuntimeException("La marca es obligatoria");
        }

        // Valida que la referencia no venga vacia
        if (referencia.getReferencia() == null || referencia.getReferencia().trim().isEmpty()) {
            throw new RuntimeException("La referencia es obligatoria");
        }

        // Valida que no exista otra referencia con la misma marca y referencia
        Optional<Referencia> existente = referenciaRepository.findByMarcaAndReferencia(
            referencia.getMarca().trim(),
            referencia.getReferencia().trim()
        );

        // Si ya existe lanza un error
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe la referencia: " + referencia.getReferencia() + " para la marca: " + referencia.getMarca());
        }

        // Limpia los espacios antes de guardar
        referencia.setMarca(referencia.getMarca().trim());
        referencia.setReferencia(referencia.getReferencia().trim());

        return referenciaRepository.save(referencia);
    }

    // Actualiza los datos de una referencia existente
    public Referencia actualizar(Long id, Referencia datos) {

        // Verifica que la referencia exista
        Referencia referencia = buscarPorId(id);

        // Valida que la marca no venga vacia
        if (datos.getMarca() == null || datos.getMarca().trim().isEmpty()) {
            throw new RuntimeException("La marca es obligatoria");
        }

        // Valida que la referencia no venga vacia
        if (datos.getReferencia() == null || datos.getReferencia().trim().isEmpty()) {
            throw new RuntimeException("La referencia es obligatoria");
        }

        // Valida que la nueva combinacion no la tenga otra referencia diferente
        Optional<Referencia> existente = referenciaRepository.findByMarcaAndReferencia(
            datos.getMarca().trim(),
            datos.getReferencia().trim()
        );

        // Si existe pero es un registro diferente lanza un error
        if (existente.isPresent() && existente.get().getId().equals(id) == false) {
            throw new RuntimeException("Ya existe otra referencia con esa marca y referencia");
        }

        // Actualiza los campos
        referencia.setMarca(datos.getMarca().trim());
        referencia.setReferencia(datos.getReferencia().trim());
        referencia.setDescripcion(datos.getDescripcion());

        return referenciaRepository.save(referencia);
    }

    // Retorna solo las referencias activas
    public List<Referencia> listarActivas() {
        // Busca todas las referencias con activo igual a 1
        return referenciaRepository.findByActivo((byte) 1);
    }

    // Retorna referencias activas filtradas por marca
    public List<Referencia> listarActivasPorMarca(String marca) {
        // Busca referencias de una marca especifica que esten activas
        return referenciaRepository.findByMarcaAndActivo(marca, (byte) 1);
    }

    // Activa una referencia
    public Referencia activar(Long id) {
        // Verifica que la referencia exista
        Referencia referencia = buscarPorId(id);

        // Cambia el estado a activo
        referencia.setActivo((byte) 1);

        return referenciaRepository.save(referencia);
    }

    // Desactiva una referencia
    public Referencia desactivar(Long id) {
        // Verifica que la referencia exista
        Referencia referencia = buscarPorId(id);

        // Cambia el estado a inactivo
        referencia.setActivo((byte) 0);

        return referenciaRepository.save(referencia);
    }

    // SOLO DESARROLLO - elimina fisicamente el registro
    public void eliminar(Long id) {
        // Verifica que la referencia exista antes de eliminar
        buscarPorId(id);
        referenciaRepository.deleteById(id);
    }

    
}
