package Controlador;

import Modelo.DTOUsuario;
import Modelo.Usuario;
import Modelo.Prestamo;
import Modelo.Ejemplar;
import Modelo.DAOGenerico;
import Modelo.DTOPrestamo;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class ControladorPrestamo {
    private final DAOGenerico<Prestamo> daoPrestamo;
    private final DAOGenerico<Ejemplar> daoEjemplar;
    private final EntityManager entityManager;

    public ControladorPrestamo(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.daoPrestamo = new DAOGenerico<>(Prestamo.class);
        this.daoEjemplar = new DAOGenerico<>(Ejemplar.class);
    }

    public DTOPrestamo devolverPrestamo(int idPrestamo) {
        try {
            // Obtener el préstamo con un JOIN FETCH para cargar el Ejemplar
            Prestamo prestamo = entityManager.createQuery(
                            "SELECT p FROM Prestamo p JOIN FETCH p.ejemplar WHERE p.id = :id", Prestamo.class)
                    .setParameter("id", idPrestamo)
                    .getSingleResult();

            if (prestamo != null) {
                if (prestamo.getFechaDevolucion() != null) {
                    System.out.println("El préstamo ya ha sido devuelto.");
                    return null;
                }

                // Establecer la fecha de devolución
                prestamo.setFechaDevolucion(LocalDate.now());
                daoPrestamo.actualizar(entityManager, prestamo);

                // Actualizar el estado del ejemplar
                Ejemplar ejemplar = prestamo.getEjemplar();
                ejemplar.setEstado("Disponible");
                daoEjemplar.actualizar(entityManager, ejemplar);

                System.out.println("✅ Préstamo devuelto correctamente.");

                LocalDate fechaLimite = prestamo.getFechaInicio().plusDays(15);
                return new DTOPrestamo(
                        prestamo.getId(),
                        prestamo.getUsuario().getDni(),
                        prestamo.getEjemplar().getId(),
                        prestamo.getFechaInicio(),
                        fechaLimite,
                        prestamo.getFechaDevolucion()
                );
            } else {
                System.out.println("No se encontró el préstamo con ID: " + idPrestamo);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DTOPrestamo> obtenerPrestamosPorUsuario(String dni) {
        try {
            List<Prestamo> prestamos = entityManager.createQuery(
                            "SELECT p FROM Prestamo p WHERE p.usuario.dni = :dni", Prestamo.class)
                    .setParameter("dni", dni)
                    .getResultList();

            return prestamos.stream()
                    .map(prestamo -> new DTOPrestamo(
                            prestamo.getId(),
                            prestamo.getUsuario().getDni(),
                            prestamo.getEjemplar().getId(),
                            prestamo.getFechaInicio(),
                            prestamo.getFechaInicio().plusDays(15),
                            prestamo.getFechaDevolucion()
                    ))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DTOPrestamo solicitarPrestamo(String dniUsuario, int idEjemplar) {
        try {
            Ejemplar ejemplar = daoEjemplar.obtenerPorId(entityManager, idEjemplar);
            if (ejemplar != null && "Disponible".equalsIgnoreCase(ejemplar.getEstado())) {

                Usuario usuario = entityManager.createQuery(
                                "SELECT u FROM Usuario u WHERE u.dni = :dni", Usuario.class)
                        .setParameter("dni", dniUsuario)
                        .getSingleResult();

                if (usuario == null) {
                    System.out.println("No se encontró el usuario con DNI: " + dniUsuario);
                    return null;
                }

                Prestamo nuevoPrestamo = new Prestamo();
                LocalDate fechaInicio = LocalDate.now();
                LocalDate fechaDevolucion = fechaInicio.plusDays(15);

                nuevoPrestamo.setFechaInicio(fechaInicio);
                nuevoPrestamo.setFechaDevolucion(fechaDevolucion); // Fecha futura
                nuevoPrestamo.setEjemplar(ejemplar);
                nuevoPrestamo.setUsuario(usuario);

                ejemplar.setEstado("Prestado");

                daoEjemplar.actualizar(entityManager, ejemplar);
                daoPrestamo.guardar(entityManager, nuevoPrestamo);

                return new DTOPrestamo(
                        nuevoPrestamo.getId(),
                        dniUsuario,
                        ejemplar.getId(),
                        nuevoPrestamo.getFechaInicio(),
                        fechaDevolucion,
                        nuevoPrestamo.getFechaDevolucion()
                );
            } else {
                System.out.println("El ejemplar no está disponible.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminarPrestamo(int idPrestamo) {
        try {
            Prestamo prestamo = daoPrestamo.obtenerPorId(entityManager, idPrestamo);
            if (prestamo == null) {
                System.out.println("❌ No se encontró el préstamo con ID: " + idPrestamo);
                return false;
            }

            Ejemplar ejemplar = prestamo.getEjemplar();
            if (ejemplar != null) {
                ejemplar.setEstado("Disponible");
                daoEjemplar.actualizar(entityManager, ejemplar);
            }

            daoPrestamo.eliminar(entityManager, prestamo);
            System.out.println("✅ Préstamo eliminado correctamente y el ejemplar está ahora disponible.");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al intentar eliminar el préstamo.");
            return false;
        }
    }
}