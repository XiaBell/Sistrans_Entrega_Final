package f3.com.example.demo.servicio;
import f3.com.example.demo.modelo.CitaMedica;
import f3.com.example.demo.repositorio.CitaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import f3.com.example.demo.repositorio.MedicoRepository;
import f3.com.example.demo.modelo.Medico;
import f3.com.example.demo.modelo.ServicioCountDTO;


@Service
public class CitaMedicaService {

    @Autowired
    private CitaMedicaRepository citaMedicaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map<String, Object>> consultarDisponibilidadServicio(String servicioId) {
        LocalDate hoy = LocalDate.now();
        LocalDate enCuatroSemanas = hoy.plusWeeks(4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String desde = hoy.format(formatter);
        String hasta = enCuatroSemanas.format(formatter);

        List<CitaMedica> citas = citaMedicaRepository.findByServicio_IdAndFechaBetween(servicioId, desde, hasta);

        if (citas != null && !citas.isEmpty()) {
            return citas.stream().map(cita -> {
                Map<String, Object> info = new HashMap<>();
                info.put("nombreServicio", cita.getServicio() != null ? cita.getServicio().getNombre() : "Desconocido");
                info.put("fecha", cita.getFecha());
                info.put("hora", cita.getHora());
                info.put("nombreIPS", "Clínica Central");

                String nombreMedico = "No asignado";
                // Siempre busca el médico por id en el repositorio
                if (cita.getMedico() != null && cita.getMedico().getId() != null) {
                    Medico medico = medicoRepository.findById(cita.getMedico().getId()).orElse(null);
                    if (medico != null && medico.getNombre() != null) {
                        nombreMedico = medico.getNombre();
                    }
                }
                info.put("nombreMedico", nombreMedico);
                return info;
            }).collect(Collectors.toList());
        }

        // Si no hay citas reales, genera horarios ficticios
        List<Map<String, Object>> disponibilidad = new java.util.ArrayList<>();
        for (LocalDate fecha = hoy; fecha.isBefore(enCuatroSemanas); fecha = fecha.plusDays(1)) {
            for (int hora = 8; hora <= 16; hora++) {
                Map<String, Object> info = new HashMap<>();
                info.put("nombreServicio", "Consulta General");
                info.put("fecha", fecha.format(formatter));
                info.put("hora", String.format("%02d:00", hora));
                info.put("nombreIPS", "Clínica Central");
                info.put("nombreMedico", "Dr. Juan Pérez");
                disponibilidad.add(info);
            }
        }
        return disponibilidad;
    }



    public List<ServicioCountDTO> obtenerTop20Servicios(String fechaInicio, String fechaFin) {
        MatchOperation match = Aggregation.match(
                Criteria.where("fecha").gte(fechaInicio).lte(fechaFin)
        );

        // Agrupa por el nombre del servicio (campo consistente)
        GroupOperation group = Aggregation.group("servicio.nombre")
                .count().as("totalSolicitudes");

        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "totalSolicitudes");
        LimitOperation limit = Aggregation.limit(20);

        Aggregation aggregation = Aggregation.newAggregation(match, group, sort, limit);

        AggregationResults<ServicioCountDTO> resultados =
                mongoTemplate.aggregate(aggregation, "citamedica", ServicioCountDTO.class);

        // Ajusta el nombre en el DTO para que sea el nombre del servicio
        return resultados.getMappedResults().stream().map(dto -> {
            return new ServicioCountDTO(
                dto.get_id() != null ? dto.get_id() : "Desconocido",
                dto.getTotalSolicitudes()
            );
        }).collect(Collectors.toList());
    }
}
