package librosa.libro.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import librosa.libro.entity.Usuario;
import librosa.libro.entity.Libro;
import librosa.libro.service.LibroService;
import librosa.libro.service.CategoriaService;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private CategoriaService categoriaService;

    // 🔥 FORM NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        if (!"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("libro", new Libro());
        model.addAttribute("categorias", categoriaService.obtenerTodas());

        return "libro-form";
    }

    // 🔥 GUARDAR
    @PostMapping("/guardar")
    public String guardarLibro(@ModelAttribute Libro libro,
                               HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/usuarios/login";
        }

        libroService.add(libro);

        return "redirect:/admin/dashboard";
    }

    // 🔥 EDITAR
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id,
                         HttpSession session,
                         Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/usuarios/login";
        }

        Libro libro = libroService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        model.addAttribute("libro", libro);
        model.addAttribute("categorias", categoriaService.obtenerTodas());

        return "libro-form";
    }

    // 🔥 ELIMINAR
    @GetMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id,
                           HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/usuarios/login";
        }

        libroService.eliminar(id);

        return "redirect:/admin/dashboard";
    }
}