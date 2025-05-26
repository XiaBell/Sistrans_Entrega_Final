package f3.com.example.demo.controller;

import f3.com.example.demo.modelo.Afiliado;
import f3.com.example.demo.modelo.CitaMedica;
import f3.com.example.demo.modelo.IPS;
import f3.com.example.demo.modelo.Medico;
import f3.com.example.demo.modelo.Orden;
import f3.com.example.demo.modelo.Servicio;
import f3.com.example.demo.modelo.ServicioCountDTO;
import f3.com.example.demo.repositorio.CitaMedicaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Optional;
import f3.com.example.demo.servicio.CitaMedicaService;

@RestController
@RequestMapping("/citas")
public class CitaMedicaController {

    private final CitaMedicaRepository repo;
    private final f3.com.example.demo.repositorio.ServicioRepository servicioRepo;
    private final f3.com.example.demo.repositorio.AfiliadoRepository afiliadoRepo;
    private final f3.com.example.demo.repositorio.MedicoRepository medicoRepo;
    private final f3.com.example.demo.repositorio.OrdenRepository ordenRepo;
    private final f3.com.example.demo.repositorio.IPSRepository ipsRepo;
    private final CitaMedicaService citaMedicaService;

    public CitaMedicaController(CitaMedicaRepository repo, f3.com.example.demo.repositorio.ServicioRepository servicioRepo,
                                 f3.com.example.demo.repositorio.AfiliadoRepository afiliadoRepo,
                                 f3.com.example.demo.repositorio.MedicoRepository medicoRepo,
                                 f3.com.example.demo.repositorio.OrdenRepository ordenRepo,
                                 f3.com.example.demo.repositorio.IPSRepository ipsRepo,
                                 CitaMedicaService citaMedicaService) {
        this.repo = repo;
        this.servicioRepo = servicioRepo;
        this.afiliadoRepo = afiliadoRepo;
        this.medicoRepo = medicoRepo;
        this.ordenRepo = ordenRepo;
        this.ipsRepo = ipsRepo;
        this.citaMedicaService = citaMedicaService;
    }

    @PostMapping
    public ResponseEntity<CitaMedica> agregarCitaMedica(@RequestBody CitaMedica citaMedica) {
        CitaMedica guardado = repo.save(citaMedica); // Esto guarda en MongoDb
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<CitaMedica>> obtenerTodos() {
        List<CitaMedica> citas = repo.findAll();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> buscarPorId(@PathVariable String id) {
        CitaMedica cita = repo.findById(id).orElse(null);
        return cita != null ? ResponseEntity.ok(cita) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCitaMedica(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> actualizarCitaMedica(@PathVariable String id, @RequestBody CitaMedica citaMedica) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        citaMedica.setId(id);
        CitaMedica actualizado = repo.save(citaMedica);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/disponibilidad/{servicioId}")
public ResponseEntity<List<LocalDateTime>> consultarDisponibilidad(@PathVariable String servicioId) {
    LocalDateTime hoy = LocalDateTime.now();
    LocalDateTime en4Semanas = hoy.plusWeeks(4);
    List<CitaMedica> citasExistentes = repo.findAll();

    List<LocalDateTime> ocupadas = citasExistentes.stream()
        .filter(c -> c.getServicio().getId().equals(servicioId))
        .map(c -> LocalDateTime.parse(c.getFecha() + "T" + c.getHora()))
        .toList();

    List<LocalDateTime> posibles = new ArrayList<>();
    for (LocalDateTime dia = hoy; dia.isBefore(en4Semanas); dia = dia.plusDays(1)) {
        for (int h = 8; h <= 16; h++) {
            LocalDateTime horario = dia.withHour(h).withMinute(0);
            if (!ocupadas.contains(horario)) {
                posibles.add(horario);
            }
        }
    }

    return ResponseEntity.ok(posibles);
}

@PostMapping("/agendar")
public ResponseEntity<?> agendarServicioSalud(@RequestBody Map<String, String> datos) {
    String servicioId = datos.get("servicioId");
    String afiliadoId = datos.get("afiliadoId");
    String medicoId = datos.get("medicoId");
    String ordenId = datos.get("ordenId");
    String fecha = datos.get("fecha");
    String hora = datos.get("hora");

    Servicio servicio = servicioRepo.findById(servicioId).orElse(null);
    if (servicio == null) return ResponseEntity.badRequest().body("Servicio no encontrado.");

    Afiliado afiliado = afiliadoRepo.findById(afiliadoId).orElse(null);
    Medico medico = medicoRepo.findById(medicoId).orElse(null);
    if (afiliado == null || medico == null) return ResponseEntity.badRequest().body("Afiliado o Médico no encontrado.");

    // Verificar si requiere orden
    boolean requiereOrden = !servicio.getNombre().equalsIgnoreCase("Consulta general")
                          && !servicio.getNombre().equalsIgnoreCase("Urgencias");

    Orden orden = null;
    if (requiereOrden) {
        if (ordenId == null || ordenId.isEmpty())
            return ResponseEntity.badRequest().body("Este servicio requiere una orden médica.");
        orden = ordenRepo.findById(ordenId).orElse(null);
        if (orden == null) return ResponseEntity.badRequest().body("Orden médica no encontrada.");
    }

    // Verificar disponibilidad
    List<CitaMedica> citasExistentes = repo.findByFechaAndHora(fecha, hora);
    for (CitaMedica cita : citasExistentes) {
        if (cita.getServicio().getId().equals(servicioId)) {
            return ResponseEntity.status(409).body("Ya hay una cita para ese servicio en esa fecha y hora.");
        }
    }

    // Crear y guardar la cita
    CitaMedica cita = new CitaMedica();
    cita.setFecha(fecha);
    cita.setHora(hora);
    cita.setEstado("Agendada");
    cita.setAfiliado(afiliado);
    cita.setMedico(medico);
    cita.setServicio(servicio);
    cita.setOrdenes(orden != null ? List.of(orden) : new ArrayList<>());

    repo.save(cita);
    return ResponseEntity.ok(cita);
}

    @GetMapping("/agenda/servicio/{servicioId}")
    public ResponseEntity<List<Map<String, Object>>> obtenerAgendaServicio(@PathVariable String servicioId) {
        List<Map<String, Object>> disponibilidad = citaMedicaService.consultarDisponibilidadServicio(servicioId);
        return ResponseEntity.ok(disponibilidad);
    }

    @GetMapping("/servicios-mas-solicitados")
public ResponseEntity<List<ServicioCountDTO>> topServicios(
        @RequestParam String desde,
        @RequestParam String hasta) {
    return ResponseEntity.ok(citaMedicaService.obtenerTop20Servicios(desde, hasta));
}

}

