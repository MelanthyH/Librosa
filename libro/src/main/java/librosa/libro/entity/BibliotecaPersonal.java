package librosa.libro.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "biblioteca_personal")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BibliotecaPersonal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private int id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro libro;
    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // "pendiente", "leyendo", "leído"
    
    @Column(name = "pagina_actual")
    private int paginaActual;
    
    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;
    
    @Column(name = "notas_resena", columnDefinition = "TEXT")
    private String notasResena;
}
