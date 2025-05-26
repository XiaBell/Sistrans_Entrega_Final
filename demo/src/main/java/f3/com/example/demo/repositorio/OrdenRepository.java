package f3.com.example.demo.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import f3.com.example.demo.modelo.Orden;

public interface OrdenRepository extends MongoRepository<Orden, String> {
    // No need to redeclare findById, as it's already inherited from MongoRepository
}
