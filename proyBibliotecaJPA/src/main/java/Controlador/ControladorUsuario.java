package Controlador;

import Modelo.DTOUsuario;
import Modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;

public class ControladorUsuario {

    private final EntityManager entityManager;

    public ControladorUsuario(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public DTOUsuario autenticarUsuario(String dni, String password) {
        try {
            TypedQuery<Usuario> query = entityManager.createQuery(
                    "SELECT u FROM Usuario u WHERE u.dni = :dni AND u.password = :password", Usuario.class);
            query.setParameter("dni", dni);
            query.setParameter("password", password);

            Usuario usuario = query.getSingleResult();

            return new DTOUsuario(
                    usuario.getDni(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getTipo(),
                    usuario.getPenalizacionHasta() != null
            );
        } catch (NoResultException e) {
            return null;
        }
    }

    public LocalDate obtenerPenalizacionUsuario(String dni) {
        try {
            Usuario usuario = entityManager.createQuery(
                            "SELECT u FROM Usuario u WHERE u.dni = :dni", Usuario.class)
                    .setParameter("dni", dni)
                    .getSingleResult();

            return usuario != null ? usuario.getPenalizacionHasta() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}