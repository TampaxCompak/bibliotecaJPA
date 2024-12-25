package Controlador;

import Modelo.DAOGenerico;
import Modelo.Libro;
import jakarta.persistence.EntityManager;

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

    public Libro obtenerLibroPorId(int id) {
        return daoLibro.obtenerPorId(entityManager, id);
    }

    public List<Libro> obtenerTodosLosLibros() {
        return daoLibro.obtenerTodos(entityManager);
    }

    public Libro obtenerLibroPorIsbn(String isbn13) {
        return daoLibro.obtenerPorCampoUnico(entityManager, "isbn", isbn13);
    }
}