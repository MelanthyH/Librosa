package librosa.libro.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import librosa.libro.entity.Usuario;
import librosa.libro.service.BibliotecaPersonalService;

@Controller
public class HomeController {

    @Autowired
    private BibliotecaPersonalService bibliotecaService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 🔥 DASHBOARD USUARIO CON SESIÓN
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        model.addAttribute("usuario", usuario);

        Map<String, Integer> stats = new HashMap<>();

        stats.put("leidos",
                bibliotecaService.obtenerLibrosPorUsuarioYEstado(
                        usuario.getId(), "leído").size());

        stats.put("enProceso",
                bibliotecaService.obtenerLibrosPorUsuarioYEstado(
                        usuario.getId(), "leyendo").size());

        stats.put("pendientes",
                bibliotecaService.obtenerLibrosPorUsuarioYEstado(
                        usuario.getId(), "pendiente").size());

        model.addAttribute("stats", stats);

        return "user-dashboard";
    }
}