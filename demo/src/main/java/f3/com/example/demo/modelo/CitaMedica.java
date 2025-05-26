package f3.com.example.demo.modelo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Embedded;
import lombok.Data;
import java.util.List;


@Document(collection = "citamedica")
@Data
public class CitaMedica {

    @Id
    private String id;
    private String fecha;
    private String estado;
    private String hora;
    @DBRef
    private Afiliado afiliado;
    @DBRef
    private Medico medico;
    @Embedded
    private Servicio servicio;
    @Embedded
    private List<Orden> ordenes;


}
