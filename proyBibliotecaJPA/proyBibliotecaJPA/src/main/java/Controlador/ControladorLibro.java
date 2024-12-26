package Controlador;

import Modelo.DAOGenerico;
import Modelo.DTOLibro;
import Modelo.Libro;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class ControladorLibro {
    private final DAOGenerico<Libro> daoLibro;
    private final EntityManager entityManager;

    public ControladorLibro(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.daoLibro = new DAOGenerico<>(Libro.class);
    }

    public Libro guardarLibro(Libro libro) {
        return daoLibro.guardar(entityManager, libro);
    }

    public Libro actualizarLibro(Libro libro) {
        return daoLibro.actualizar(entityManager, libro);
    }

    public void eliminarLibro(Libro libro) {
        daoLibro.eliminar(entityManager, libro);
    }

    public List<DTOLibro> obtenerTodosLosLibros() {
        List<Libro> libros = daoLibro.obtenerTodos(entityManager);
        List<DTOLibro> dtoLibros = new ArrayList<>();
        for (Libro libro : libros) {
            int ejemplaresDisponibles = libro.getEjemplars().size();
            dtoLibros.add(new DTOLibro(libro.getIsbn(), libro.getTitulo(), libro.getAutor(), ejemplaresDisponibles));
        }
        return dtoLibros;
    }

    public Libro obtenerLibroPorIsbn(String isbn13) {
        return daoLibro.obtenerPorCampoUnico(entityManager, "isbn", isbn13);
    }
}
