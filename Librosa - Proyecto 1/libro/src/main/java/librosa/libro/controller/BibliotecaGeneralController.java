package librosa.libro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import librosa.libro.entity.Libro;
import librosa.libro.entity.Usuario;
import librosa.libro.service.BibliotecaPersonalService;
import librosa.libro.service.CategoriaService;
import librosa.libro.service.LibroService;



@Controller
@RequestMapping("/biblioteca-general")
public class BibliotecaGeneralController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private BibliotecaPersonalService bibliotecaService;

    @GetMapping
    public String mostrarBibliotecaGeneral(@RequestParam(required = false) Integer categoriaId,HttpSession session,Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        List<Libro> libros = (categoriaId != null)
                ? libroService.obtenerPorCategoria(categoriaId)
                : libroService.obtenerTodos();

        model.addAttribute("libros", libros);
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("categoriaSeleccionada", categoriaId);

        return "biblioteca-general";
    }

    @PostMapping("/agregar")
    public String agregarALibroteca(@RequestParam int libroId,
                                    HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        bibliotecaService.agregarLibro(libroId, usuario.getId());

        return "redirect:/mi-biblioteca";
    }
}