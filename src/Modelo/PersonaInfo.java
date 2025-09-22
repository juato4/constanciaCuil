package Modelo;

public class PersonaInfo {

    private final String nombre;
    private final String cuil;
    private final String edad;
    private final String provincia;
    private final String localidad;
    private final String detalleUrl;

    public PersonaInfo(String nombre, String cuil, String edad, String provincia, String localidad, String detalleUrl) {
        this.nombre = nombre;
        this.cuil = cuil;
        this.edad = edad;
        this.provincia = provincia;
        this.localidad = localidad;
        this.detalleUrl = detalleUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCuil() {
        return cuil;
    }

    public String getEdad() {
        return edad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getDetalleUrl() {
        return detalleUrl;
    }

    public String getDni() {
        String digits = cuil == null ? "" : cuil.replaceAll("\\D", "");
        if (digits.length() >= 10) {
            return digits.substring(2, 10);
        }
        return "";
    }
}
