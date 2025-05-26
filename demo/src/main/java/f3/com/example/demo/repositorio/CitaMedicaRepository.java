package f3.com.example.demo.repositorio;

import f3.com.example.demo.modelo.CitaMedica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CitaMedicaRepository extends MongoRepository<CitaMedica, String> {


   List<CitaMedica> findByFechaAndHora(String fecha, String hora);
   List<CitaMedica> findByServicio_IdAndFechaBetween(String servicioId, String desde, String hasta);

}

