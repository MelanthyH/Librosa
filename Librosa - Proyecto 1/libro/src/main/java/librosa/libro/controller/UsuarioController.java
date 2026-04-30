package librosa.libro.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import librosa.libro.entity.Usuario;
import librosa.libro.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // LOGIN
    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String correo,
                             @RequestParam String contraseña,
                             HttpSession session,
                             Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario u = usuarioOpt.get();

            if (u.getContraseña().equals(contraseña)) {

                // 🔥 GUARDAR EN SESIÓN
                session.setAttribute("usuario", u);

                if ("admin".equalsIgnoreCase(u.getTipoUsuario())) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/dashboard";
                }
            }
        }

        model.addAttribute("error", "Credenciales incorrectas");
        return "login";
    }

    // PERFIL (YA SIN ID)
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @GetMapping("/login")
public String login() {
    return "login";
}
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/usuarios/login";
    }
}