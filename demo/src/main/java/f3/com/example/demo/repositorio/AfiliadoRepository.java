package f3.com.example.demo.repositorio;

import f3.com.example.demo.modelo.Afiliado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AfiliadoRepository extends MongoRepository<Afiliado, String> {
    Afiliado findByNumeroDocumento(String numeroDocumento);
}
