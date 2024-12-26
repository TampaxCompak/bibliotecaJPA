package Modelo;

public class DTOEjemplar {
    private Long id;
    private String isbn;
    private String estado;

    public DTOEjemplar(Long id, String isbn, String estado) {
        this.id = id;
        this.isbn = isbn;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}