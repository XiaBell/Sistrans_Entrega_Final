package f3.com.example.demo.controller;
import f3.com.example.demo.modelo.Medico;
import f3.com.example.demo.repositorio.MedicoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoRepository repo;

    public MedicoController(MedicoRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Medico> agregarMedico(@RequestBody Medico medico) {
        Medico guardado = repo.save(medico); // Esto guarda en MongoDb
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Medico>> obtenerTodos() {
        List<Medico> medicos = repo.findAll();
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{documento}")
    public Medico buscarPorDocumento(@PathVariable String documento) {
        return repo.findByRegistroMedico(documento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> actualizarMedico(@PathVariable String id, @RequestBody Medico medico) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        medico.setId(id);
        Medico actualizado = repo.save(medico);
        return ResponseEntity.ok(actualizado);
    }
}
