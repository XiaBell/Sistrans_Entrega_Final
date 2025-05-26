package f3.com.example.demo.repositorio;
import f3.com.example.demo.modelo.Servicio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServicioRepository extends MongoRepository<Servicio, String> {
    Servicio findByNombre(String nombre);
}