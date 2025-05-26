package f3.com.example.demo.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "afiliado")
@Data
public class Afiliado {

    @Id
    private String id;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String direccion;
    private String telefono;
    private String tipoAfiliado;
    private String relacion; // Puede ser "Esposa", "Hijo", etc. si es beneficiario y es "No aplica" si es contribuyente
    


}
