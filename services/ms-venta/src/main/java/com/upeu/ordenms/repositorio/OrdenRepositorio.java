package com.upeu.ordenms.repositorio;

import com.upeu.ordenms.entidad.Orden;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdenRepositorio extends JpaRepository<Orden, Long> {

    @EntityGraph(attributePaths = "detalles")
    List<Orden> findAllByOrderByFechaVentaDesc();

    @EntityGraph(attributePaths = "detalles")
    @Query("SELECT o FROM Orden o WHERE o.id = :id")
    Optional<Orden> findDetalleById(@Param("id") Long id);
}
