package Modelo;

import java.time.LocalDate;

public class DTOPrestamo {
    private int id;
    private String usuarioDni;
    private int ejemplarId;
    private LocalDate fechaInicio;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;

    public DTOPrestamo(int id, String usuarioDni, int ejemplarId, LocalDate fechaInicio, LocalDate fechaLimite, LocalDate fechaDevolucion) {
        this.id = id;
        this.usuarioDni = usuarioDni;
        this.ejemplarId = ejemplarId;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuarioDni() {
        return usuarioDni;
    }

    public void setUsuarioDni(String usuarioDni) {
        this.usuarioDni = usuarioDni;
    }

    public int getEjemplarId() {
        return ejemplarId;
    }

    public void setEjemplarId(int ejemplarId) {
        this.ejemplarId = ejemplarId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}