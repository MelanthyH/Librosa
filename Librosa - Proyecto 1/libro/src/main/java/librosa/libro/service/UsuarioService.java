package librosa.libro.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import librosa.libro.entity.Usuario;
import librosa.libro.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {
    
    private UsuarioRepository usuarioRepository;
    
    public Usuario registrar(Usuario usuario) {
        // Validar que no exista email duplicado
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya existe");
        }
        usuario.setTipoUsuario("usuario"); // Por defecto
        return usuarioRepository.save(usuario);
    }
    
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    
    public Optional<Usuario> obtenerPorId(int id) {
        return usuarioRepository.findById(id);
    }
    
    public Usuario actualizar(int id, Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findById(id);
        if (existente.isPresent()) {
            Usuario u = existente.get();
            u.setNombre(usuario.getNombre());
            u.setCorreo(usuario.getCorreo());
            return usuarioRepository.save(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    public void eliminar(int id) {
        usuarioRepository.deleteById(id);
    }
}
