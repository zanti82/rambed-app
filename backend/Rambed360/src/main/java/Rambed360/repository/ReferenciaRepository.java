package Rambed360.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Rambed360.entity.Referencia;

public interface ReferenciaRepository extends JpaRepository<Referencia, Long> {

    // Busca referencias por marca
    List<Referencia> findByMarca(String marca);

    // Busca una referencia por marca y referencia para validar duplicados
    Optional<Referencia> findByMarcaAndReferencia(String marca, String referencia);

    // Busca referencias por marca y estado
    List<Referencia> findByMarcaAndActivo(String marca, Byte activo);

     // Busca referencias por estado activo o inactivo
     List<Referencia> findByActivo(Byte activo);

}