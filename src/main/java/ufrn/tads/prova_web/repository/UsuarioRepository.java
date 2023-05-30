package ufrn.tads.prova_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.tads.prova_web.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByLogin(String login);
}
