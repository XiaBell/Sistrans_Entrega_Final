package f3.com.example.demo.repositorio;

import f3.com.example.demo.modelo.Medico;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicoRepository extends MongoRepository<Medico, String> {
    Medico findByRegistroMedico(String registroMedico);
    List<Medico> findByEspecialidad(String especialidad);
}
