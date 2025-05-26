package f3.com.example.demo.modelo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "ips")
@Data
public class IPS {

    @Id
    private String id;
    private String nit;
    private String nombre;
    private String telefono;
    private String direccion;

    @DBRef
    private List<Servicio> servicios;

    @DBRef
    private List<Medico> medicos;


}