package librosa.libro.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import librosa.libro.entity.Categoria;
import librosa.libro.repository.CategoriaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoriaService {
    
    private CategoriaRepository categoriaRepository;
    
    public Categoria add(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    
    public List<Categoria> obtenerTodas() {
    return categoriaRepository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .toList();
}
    
    public Optional<Categoria> obtenerPorId(int id) {
        return categoriaRepository.findById(id);
    }
    
    public Categoria actualizar(int id, Categoria categoria) {
        Optional<Categoria> existente = categoriaRepository.findById(id);
        if (existente.isPresent()) {
            Categoria cat = existente.get();
            cat.setNombreCategoria(categoria.getNombreCategoria());
            return categoriaRepository.save(cat);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }
    }
    
    public void eliminar(int id) {
        categoriaRepository.deleteById(id);
    }
}
