package Modelo;

public class DTOUsuario {
    private String dni;
    private String nombre;
    private String email;
    private String tipo;
    private boolean tienePenalizacionActiva;

    public DTOUsuario(String dni, String nombre, String email, String tipo, boolean tienePenalizacionActiva) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.tipo = tipo;
        this.tienePenalizacionActiva = tienePenalizacionActiva;
    }

    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isTienePenalizacionActiva() {
        return tienePenalizacionActiva;
    }

    public void setTienePenalizacionActiva(boolean tienePenalizacionActiva) {
        this.tienePenalizacionActiva = tienePenalizacionActiva;
    }
}