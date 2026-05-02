package librosa.libro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import librosa.libro.entity.Categoria;
import librosa.libro.entity.Libro;
import librosa.libro.entity.Usuario;
import librosa.libro.service.UsuarioService;
import librosa.libro.service.LibroService;
import librosa.libro.service.CategoriaService;

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private CategoriaService generoService;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // 🔥 si no hay sesión
        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        // 🔥 opcional: seguridad admin
        if (!"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/dashboard";
        }

        List<Usuario> usuarios = usuarioService.obtenerTodos();
        List<Libro> libros = libroService.obtenerTodos();

        List<Categoria> generos = generoService.obtenerTodas()
                .stream()
                .filter(g -> g != null)
                .toList();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("generos", generos);
        model.addAttribute("libros", libros);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsuarios", usuarios.size());
        stats.put("totalLibros", libros.size());
        stats.put("totalGeneros", generoService.obtenerTodas().size());

        model.addAttribute("stats", stats);

        return "admin-dashboard";
    }
}
