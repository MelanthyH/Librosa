package librosa.libro.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import librosa.libro.entity.Libro;
import librosa.libro.repository.LibroRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibroService {

    private LibroRepository libroRepository;

    public Libro add(Libro libro) {
        return libroRepository.save(libro);
    }

    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> obtenerPorId(int id) {
        return libroRepository.findById(id);
    }

    public List<Libro> obtenerPorCategoria(int categoriaId) {
        return libroRepository.findByCategoria_Id(categoriaId);
    }

    public Libro actualizar(int id, Libro libro) {
        Optional<Libro> existente = libroRepository.findById(id);
        if (existente.isPresent()) {
            Libro l = existente.get();
            l.setTitulo(libro.getTitulo());
            l.setAutor(libro.getAutor());
            l.setDescripcion(libro.getDescripcion());
            l.setCategoria(libro.getCategoria());
            return libroRepository.save(l);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
    }

    public void eliminar(int id) {
        libroRepository.deleteById(id);
    }
}
