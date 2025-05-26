package f3.com.example.demo.controller;

import f3.com.example.demo.modelo.IPS;
import f3.com.example.demo.repositorio.IPSRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ipses")
public class IPSController {

    private final IPSRepository repo;

    public IPSController(IPSRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<IPS> agregarIPS(@RequestBody IPS ips) {
        IPS guardado = repo.save(ips); // Esto guarda en MongoDb
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<IPS>> obtenerTodos() {
        List<IPS> ipsList = repo.findAll();
        return ResponseEntity.ok(ipsList);
    }

    @GetMapping("/{nit}")
    public IPS buscarPorNit(@PathVariable String nit) {
        return repo.findByNit(nit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarIPS(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IPS> actualizarIPS(@PathVariable String id, @RequestBody IPS ips) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ips.setId(id);
        IPS actualizado = repo.save(ips);
        return ResponseEntity.ok(actualizado);
    }
}

