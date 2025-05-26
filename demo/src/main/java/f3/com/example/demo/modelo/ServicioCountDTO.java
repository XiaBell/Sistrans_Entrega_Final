package f3.com.example.demo.modelo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ServicioCountDTO {
    @JsonIgnore
    private String _id; // Usado solo para el mapeo interno
    private String nombre;
    private int totalSolicitudes;

    public ServicioCountDTO() {}

    public ServicioCountDTO(String nombre, int totalSolicitudes) {
        this.nombre = nombre;
        this.totalSolicitudes = totalSolicitudes;
    }
}

