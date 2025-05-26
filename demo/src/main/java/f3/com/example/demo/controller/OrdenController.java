package f3.com.example.demo.controller;

import f3.com.example.demo.modelo.Orden;
import f3.com.example.demo.repositorio.OrdenRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    private final OrdenRepository repo;

    public OrdenController(OrdenRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Orden> agregarOrden(@RequestBody Orden orden) {
        Orden guardado = repo.save(orden); // Esto guarda en MongoDb
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Orden>> obtenerTodos() {
        List<Orden> ordenes = repo.findAll();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> buscarPorId(@PathVariable String id) {
        Orden orden = repo.findById(id).orElse(null);
        return orden != null ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizarOrden(@PathVariable String id, @RequestBody Orden orden) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orden.setId(id);
        Orden actualizado = repo.save(orden);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Orden> registrarOrden(@RequestBody Orden orden) {
    orden.setFecha(LocalDate.now().toString());
    orden.setEstado("Pendiente");
    Orden guardado = repo.save(orden);
    return ResponseEntity.ok(guardado);
}

}
