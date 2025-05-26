package f3.com.example.demo.modelo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Document(collection = "servicio")
@Data
public class Servicio {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private String especialidad;

}
