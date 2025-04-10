package main.java.com.exemplo.usuario.database;

import org.springframework.data.jpa.repository.JpaRepository;
import br.net.web.project.project.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findById(int id);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByPerfil(String perfil);
    List<Usuario> findByIdAndPerfil(Integer id, String perfil);

    Optional<Usuario> findByPerfilAndIdNot(String perfil, Integer id);
}

