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
    private final DAOGenerico<Usuario> daoUsuario;
    private final EntityManager entityManager;

    public ControladorPrestamo(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.daoPrestamo = new DAOGenerico<>(Prestamo.class);
        this.daoEjemplar = new DAOGenerico<>(Ejemplar.class);
        this.daoUsuario = new DAOGenerico<>(Usuario.class);
    }

    // === MÉTODOS DE NEGOCIO ===

    public DTOPrestamo solicitarPrestamo(String dniUsuario, int idEjemplar) {
        try {
            Ejemplar ejemplar = daoEjemplar.obtenerPorId(entityManager, idEjemplar);
            if (ejemplar == null || !"Disponible".equalsIgnoreCase(ejemplar.getEstado())) {
                System.out.println("❌ El ejemplar no está disponible o no existe.");
                return null;
            }

            Usuario usuario = daoUsuario.obtenerPorId(entityManager, dniUsuario);
            if (usuario == null) {
                System.out.println("❌ No se encontró el usuario con DNI: " + dniUsuario);
                return null;
            }

            Prestamo nuevoPrestamo = new Prestamo();
            LocalDate fechaInicio = LocalDate.now();
            LocalDate fechaDevolucion = fechaInicio.plusDays(15);

            nuevoPrestamo.setFechaInicio(fechaInicio);
            nuevoPrestamo.setFechaDevolucion(fechaDevolucion);
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

    public List<DTOPrestamo> obtenerPenalizacionesPorUsuario(String dni) {
        try {
            List<Prestamo> prestamos = entityManager.createQuery(
                            "SELECT p FROM Prestamo p WHERE p.usuario.dni = :dni AND p.fechaDevolucion IS NULL AND p.fechaInicio < :fechaLimite",
                            Prestamo.class)
                    .setParameter("dni", dni)
                    .setParameter("fechaLimite", LocalDate.now().minusDays(15))
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


    public boolean renovarPrestamo(int idPrestamo) {
        try {
            Prestamo prestamo = daoPrestamo.obtenerPorId(entityManager, idPrestamo);
            if (prestamo == null) {
                System.out.println("❌ No se encontró el préstamo con ID: " + idPrestamo);
                return false;
            }

            if (prestamo.getFechaDevolucion() != null) {
                System.out.println("❌ El préstamo ya fue devuelto, no puede renovarse.");
                return false;
            }

            LocalDate nuevaFechaDevolucion = prestamo.getFechaInicio().plusDays(30);
            prestamo.setFechaDevolucion(nuevaFechaDevolucion);
            daoPrestamo.actualizar(entityManager, prestamo);

            System.out.println("✅ Préstamo renovado hasta: " + nuevaFechaDevolucion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al intentar renovar el préstamo.");
            return false;
        }
    }

    public boolean devolverPrestamo(int idPrestamo) {
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

            prestamo.setFechaDevolucion(LocalDate.now());
            daoPrestamo.actualizar(entityManager, prestamo);

            System.out.println("✅ Préstamo devuelto correctamente.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al intentar devolver el préstamo.");
            return false;
        }
    }
}
