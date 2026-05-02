package librosa.libro.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import librosa.libro.entity.Usuario;
import librosa.libro.entity.BibliotecaPersonal;
import librosa.libro.service.BibliotecaPersonalService;

@Controller
@RequestMapping("/mi-biblioteca")
public class BibliotecaController {

    @Autowired
    private BibliotecaPersonalService bibliotecaService;

    // 🔥 OBTENER BIBLIOTECA DESDE SESIÓN
    @GetMapping
    public String mostrarBiblioteca(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        List<BibliotecaPersonal> libros
                = bibliotecaService.obtenerLibrosPorUsuario(usuario.getId());

        model.addAttribute("todos", libros);
        model.addAttribute("usuario", usuario);

        return "mi-biblioteca";
    }

    // AGREGAR LIBRO (SESSION)
    @PostMapping("/agregar")
    public String agregarLibro(@RequestParam int libroId,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        // 🔥 debug opcional
        System.out.println("LIBRO ID: " + libroId);
        System.out.println("USUARIO ID: " + usuario.getId());

        bibliotecaService.agregarLibro(libroId, usuario.getId());

        return "redirect:/mi-biblioteca";
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        bibliotecaService.eliminarLibro(id);

        return "redirect:/mi-biblioteca";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable int id,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        BibliotecaPersonal registro = bibliotecaService.obtenerPorId(id);

        // 🔒 seguridad: evitar editar registros de otro usuario
        if (registro == null || registro.getUsuario().getId() != usuario.getId()) {
            return "redirect:/mi-biblioteca";
        }

        model.addAttribute("registro", registro);
        model.addAttribute("usuario", usuario);

        return "mibiblioteca-editar";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarRegistro(@PathVariable int id,
            @RequestParam String estado,
            @RequestParam(required = false) Integer paginaActual,
            @RequestParam(required = false) String notas,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        BibliotecaPersonal registro = bibliotecaService.obtenerPorId(id);

        // 🔒 seguridad
        if (registro == null || registro.getUsuario().getId() != usuario.getId()) {
            return "redirect:/mi-biblioteca";
        }

        bibliotecaService.actualizarRegistro(id, estado, paginaActual, notas);

        return "redirect:/mi-biblioteca";
    }
}
