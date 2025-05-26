package f3.com.example.demo.controller;
import f3.com.example.demo.modelo.Afiliado;
import f3.com.example.demo.repositorio.AfiliadoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/afiliados")
public class AfiliadoController {

    private final AfiliadoRepository repo;

    public AfiliadoController(AfiliadoRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Afiliado> agregarAfiliado(@RequestBody Afiliado afiliado) {
        Afiliado guardado = repo.save(afiliado); // Esto guarda en MongoDb
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Afiliado>> obtenerTodos() {
        List<Afiliado> afiliados = repo.findAll();
        return ResponseEntity.ok(afiliados);
    }

    @GetMapping("/{documento}")
    public Afiliado buscarPorDocumento(@PathVariable String documento) {
        return repo.findByNumeroDocumento(documento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAfiliado(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Afiliado> actualizarAfiliado(@PathVariable String id, @RequestBody Afiliado afiliado) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        afiliado.setId(id);
        Afiliado actualizado = repo.save(afiliado);
        return ResponseEntity.ok(actualizado);
    }
}
