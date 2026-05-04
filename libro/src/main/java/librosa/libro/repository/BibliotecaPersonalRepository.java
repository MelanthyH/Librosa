package librosa.libro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import librosa.libro.entity.BibliotecaPersonal;
import java.util.List;

@Repository
public interface BibliotecaPersonalRepository extends JpaRepository<BibliotecaPersonal, Integer> {
    List<BibliotecaPersonal> findByUsuario_Id(int usuarioId);
    List<BibliotecaPersonal> findByUsuario_IdAndEstado(int usuarioId, String estado);
}
