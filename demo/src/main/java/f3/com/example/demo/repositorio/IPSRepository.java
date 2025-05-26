package f3.com.example.demo.repositorio;

import f3.com.example.demo.modelo.IPS;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPSRepository extends MongoRepository<IPS, String> {
    IPS findByNit(String nit);
    List<IPS> findByServicios_Id(String servicioId);
}
