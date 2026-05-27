package com.example.MediturnoNew.Repository;


import com.example.MediturnoNew.Model.Especialidad;
import com.example.MediturnoNew.Model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /** Busca un médico por el ID de su usuario asociado. */
    Optional<Medico> findByUsuarioId(Long usuarioId);

    /**
     * Busca todos los médicos que tengan una especialidad determinada.
     * @param especialidad la especialidad que se desea filtrar
     * @return lista de médicos que la ejercen
     */
    @Query(" SELECT m FROM Medico m Join m.especialidades e WHERE e = :especialidad")
    List<Medico> findByEspecialidad(@Param("especialidad") Especialidad especialidad);
}
