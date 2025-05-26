package f3.com.example.demo.controller;

import f3.com.example.demo.modelo.Servicio;
import f3.com.example.demo.repositorio.ServicioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioRepository repo;

    public ServicioController(ServicioRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Servicio> agregarServicio(@RequestBody Servicio servicio) {
        Servicio guardado = repo.save(servicio); // Esto guarda en MongoDb
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> obtenerTodos() {
        List<Servicio> servicios = repo.findAll();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> buscarPorId(@PathVariable String id) {
        Servicio servicio = repo.findById(id).orElse(null);
        return servicio != null ? ResponseEntity.ok(servicio) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable String id, @RequestBody Servicio servicio) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        servicio.setId(id);
        Servicio actualizado = repo.save(servicio);
        return ResponseEntity.ok(actualizado);
    }
}

