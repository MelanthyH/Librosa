package librosa.libro.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import librosa.libro.entity.*;
import librosa.libro.service.*;
import java.util.*;

@Controller
@RequestMapping("/sugerencias-externas")
public class SugerenciaExternaController {

    @Autowired
    private LibroService libroService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private BibliotecaPersonalService bibliotecaService;

    // PASO 1: Buscar libros en Open Library
    @GetMapping("/buscar")
    public String buscar(@RequestParam String tema, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/usuarios/login";

        try {
            String url = "https://openlibrary.org/search.json?q="
                    + tema + "&limit=6";
            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(url, Map.class);
            List<Map> docs = (List<Map>) response.get("docs");

            List<Map<String, Object>> sugerencias = new ArrayList<>();
            for (Map doc : docs) {
                Map<String, Object> libro = new HashMap<>();
                libro.put("titulo", doc.getOrDefault("title", "Sin título"));
                libro.put("autor", doc.containsKey("author_name")
                        ? ((List) doc.get("author_name")).get(0)
                        : "Autor desconocido");
                libro.put("portada", doc.containsKey("cover_i")
                        ? "https://covers.openlibrary.org/b/id/" + doc.get("cover_i") + "-M.jpg"
                        : null);
                sugerencias.add(libro);
            }

            model.addAttribute("sugerencias", sugerencias);
            model.addAttribute("tema", tema);

        } catch (Exception e) {
            model.addAttribute("sugerencias", new ArrayList<>());
            model.addAttribute("error", "No se pudo conectar con Open Library.");
        }

        return "sugerencias-externas";
    }

    // PASO 2: Mostrar formulario para elegir categoría
    @GetMapping("/confirmar")
    public String confirmar(@RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam(required = false) String portada,
            Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/usuarios/login";

        model.addAttribute("titulo", titulo);
        model.addAttribute("autor", autor);
        model.addAttribute("portada", portada);
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "confirmar-sugerencia";
    }

    // PASO 3: Guardar en BD y agregar a biblioteca del usuario
    @PostMapping("/guardar")
    public String guardar(@RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam int categoriaId, @RequestParam(required = false) String portada,
            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/usuarios/login";

        // Buscar si el libro ya existe para no duplicar
        List<Libro> existentes = libroService.obtenerTodos()
                .stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
                .toList();

        Libro libro;
        if (!existentes.isEmpty()) {
            libro = existentes.get(0);
        } else {
            // Crear el libro nuevo en la BD
            Categoria cat = categoriaService.obtenerTodas()
                    .stream()
                    .filter(c -> c.getId() == categoriaId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setDescripcion("Libro agregado desde sugerencias externas.");
            libro.setCategoria(cat);
            libro.setPortadaUrl(portada);
            libro = libroService.add(libro);
        }

        // Agregar a la biblioteca personal del usuario
        bibliotecaService.agregarLibro(libro.getId(), usuario.getId());

        return "redirect:/mi-biblioteca";
    }
}