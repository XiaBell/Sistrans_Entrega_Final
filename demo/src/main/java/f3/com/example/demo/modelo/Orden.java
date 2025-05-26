package f3.com.example.demo.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Data;

@Document(collection = "orden")
@Data
public class Orden {

    @Id
    private String id;
    private String fecha;
    private String estado;
    @DBRef
    private Medico medico;
    @DBRef
    private Afiliado afiliado;
    @DBRef
    private Servicio servicio;
    private String observaciones;

}
