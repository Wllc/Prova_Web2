package ufrn.tads.prova_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.tads.prova_web.model.Sapato;

import java.util.List;

public interface SapatoRepository extends JpaRepository<Sapato,String> {
    List<Sapato> findByDeletedIsNull();
}
