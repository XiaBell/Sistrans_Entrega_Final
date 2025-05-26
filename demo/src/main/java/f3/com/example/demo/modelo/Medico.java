package f3.com.example.demo.modelo;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "medico")
@Data
public class Medico {
    @Id
    private String id;
    private String nombre;
    private String registroMedico;
    private String especialidad;

    @DBRef
    private List<IPS> IPS;

}