package librosa.libro.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

import librosa.libro.entity.BibliotecaPersonal;
import librosa.libro.entity.Libro;
import librosa.libro.entity.Usuario;
import librosa.libro.repository.BibliotecaPersonalRepository;
import librosa.libro.repository.LibroRepository;
import librosa.libro.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BibliotecaPersonalService {

    private final BibliotecaPersonalRepository bibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    // 📌 AGREGAR LIBRO
    public BibliotecaPersonal agregarLibro(int libroId, int usuarioId) {

    BibliotecaPersonal bp = new BibliotecaPersonal();

    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no existe"));

    Libro libro = libroRepository.findById(libroId)
            .orElseThrow(() -> new RuntimeException("Libro no existe"));

    bp.setUsuario(usuario);
    bp.setLibro(libro);

    bp.setEstado("PENDIENTE");
    bp.setPaginaActual(0);
    bp.setNotasResena("");
    bp.setFechaActualizacion(LocalDate.now());

    return bibliotecaRepository.save(bp);
}
    // 📌 LISTAR LIBROS
    public List<BibliotecaPersonal> obtenerLibrosPorUsuario(int usuarioId) {
        return bibliotecaRepository.findByUsuario_Id(usuarioId);
    }

    // 📌 FILTRAR POR ESTADO
    public List<BibliotecaPersonal> obtenerLibrosPorUsuarioYEstado(int usuarioId, String estado) {
        return bibliotecaRepository.findByUsuario_IdAndEstado(usuarioId, estado);
    }

    // 📌 OBTENER POR ID
    public BibliotecaPersonal obtenerPorId(int id) {
        return bibliotecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
    }

    // 📌 EDITAR COMPLETO (ESTO TE FALTABA)
    public BibliotecaPersonal actualizarRegistro(int id,
                                                 String estado,
                                                 Integer paginaActual,
                                                 String notas) {

        BibliotecaPersonal bp = bibliotecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        if (estado != null) {
            bp.setEstado(estado);
        }

        if (paginaActual != null) {
            bp.setPaginaActual(paginaActual);
        }

        if (notas != null) {
            bp.setNotasResena(notas);
        }

        bp.setFechaActualizacion(LocalDate.now());

        return bibliotecaRepository.save(bp);
    }

    // 📌 ELIMINAR
    public void eliminarLibro(int id) {
        bibliotecaRepository.deleteById(id);
    }

}
