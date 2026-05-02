package librosa.libro.controller;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import librosa.libro.entity.Usuario;
import librosa.libro.entity.Categoria;
import librosa.libro.service.CategoriaService;

@Controller
@RequestMapping("/generos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // 🔥 LISTAR
    @GetMapping
    public String listar(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        List<Categoria> generos = categoriaService.obtenerTodas();
        model.addAttribute("generos", generos);

        return "admin-dashboard";
    }

    // 🔥 NUEVO
    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("genero", new Categoria());

        return "editar-genero";
    }

    // 🔥 EDITAR
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/dashboard";
        }

        Categoria genero = categoriaService.obtenerPorId(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Género no encontrado"));

        model.addAttribute("genero", genero);

        return "editar-genero";
    }

    // 🔥 GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/usuarios/login";
        }

        if (categoria.getId() == 0) {
            categoriaService.add(categoria);
        } else {
            categoriaService.actualizar(categoria.getId(), categoria);
        }

        return "redirect:/admin/dashboard";
    }

    // 🔥 ELIMINAR
    @GetMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return "redirect:/usuarios/login";
        }

        categoriaService.eliminar(id);

        return "redirect:/admin/dashboard";
    }
}
