package icesi.i2t.leishmaniasisst.listapacientes;

/**
 * Created by Domiciano on 17/05/2016.
 */
public class PacienteListModel {
    private String id;
    private String nombre;
    private String dia;
    private String totalTratamiento;
    private boolean isActive;
    private boolean isEvaluated;

    public PacienteListModel(String id, String nombre, String dia, String totalTratamiento, boolean isActive, boolean isEvaluated) {
        this.id = id;
        this.nombre = nombre;
        this.dia = dia;
        this.totalTratamiento = totalTratamiento;
        this.isActive = isActive;
        this.isEvaluated = isEvaluated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTotalTratamiento() {
        return totalTratamiento;
    }

    public void setTotalTratamiento(String totalTratamiento) {
        this.totalTratamiento = totalTratamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
