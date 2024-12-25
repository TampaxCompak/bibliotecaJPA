package Vista;

import Controlador.ControladorPrestamo;
import Controlador.ControladorUsuario;
import Modelo.DTOPrestamo;
import Modelo.DTOUsuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Consola {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidad-biblioteca");
    private static EntityManager em = emf.createEntityManager();

    public static EntityManager getEntityManager() {
        return em;
    }

    private static void iniciar() {
        Scanner scanner = new Scanner(System.in);
        ControladorUsuario controladorUsuario = new ControladorUsuario(em);

        try {
            System.out.print("Ingrese su DNI: ");
            String dni = scanner.nextLine();

            System.out.print("Ingrese su contraseña: ");
            String password = scanner.nextLine();

            DTOUsuario usuario = controladorUsuario.autenticarUsuario(dni, password);

            if (usuario == null) {
                System.out.println("❌ Credenciales incorrectas. Acceso denegado.");
                return;
            }

            System.out.println("\nBienvenido, " + usuario.getNombre() + " (" + usuario.getTipo() + ")");

            if (usuario.getTipo().equalsIgnoreCase("Administrador")) {
                mostrarMenuAdministrador(scanner);
            } else {
                mostrarMenuUsuario(scanner, usuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            if (em != null && em.isOpen()) {
                em.close();
            }
            emf.close();
        }
    }

    private static void mostrarMenuAdministrador(Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Gestión de Usuarios");
            System.out.println("2. Gestión de Libros");
            System.out.println("3. Gestión de Ejemplares");
            System.out.println("4. Gestión de Préstamos");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    MenuUsuarios.mostrarMenu(em);
                    break;
                case 2:
                    MenuLibros.mostrarMenu(em);
                    break;
                case 3:
                    MenuEjemplares.mostrarMenu(em);
                    break;
                case 4:
                    MenuPrestamos.mostrarMenu(em);
                    break;
                case 5:
                    System.out.println("¡Gracias por usar el sistema de biblioteca!");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 5);
    }

    private static void mostrarMenuUsuario(Scanner scanner, DTOUsuario usuario) {
        int opcion;
        ControladorPrestamo controladorPrestamo = new ControladorPrestamo(em);
        ControladorUsuario controladorUsuario = new ControladorUsuario(em);

        do {
            System.out.println("\n=== MENÚ USUARIO ===");
            System.out.println("1. Ver mis préstamos actuales");
            System.out.println("2. Ver mis penalizaciones");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    mostrarPrestamosUsuario(controladorPrestamo, usuario.getDni());
                    break;
                case 2:
                    mostrarPenalizacionesUsuario(controladorUsuario, usuario.getDni());
                    break;
                case 3:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 3);
    }

    private static void mostrarPrestamosUsuario(ControladorPrestamo controladorPrestamo, String dni) {
        List<DTOPrestamo> prestamos = controladorPrestamo.obtenerPrestamosPorUsuario(dni);

        if (prestamos == null || prestamos.isEmpty()) {
            System.out.println("No tiene préstamos activos.");
        } else {
            for (DTOPrestamo prestamo : prestamos) {
                System.out.println("ID: " + prestamo.getId() +
                        ", Ejemplar ID: " + prestamo.getEjemplarId() +
                        ", Fecha Inicio: " + prestamo.getFechaInicio() +
                        ", Fecha Límite: " + prestamo.getFechaLimite() +
                        ", Fecha Devolución: " + prestamo.getFechaDevolucion());
            }
        }
    }

    private static void mostrarPenalizacionesUsuario(ControladorUsuario controladorUsuario, String dni) {
        LocalDate penalizacionHasta = controladorUsuario.obtenerPenalizacionUsuario(dni);

        if (penalizacionHasta != null && penalizacionHasta.isAfter(LocalDate.now())) {
            System.out.println("Tienes una penalización activa hasta: " + penalizacionHasta);
        } else {
            System.out.println("No tienes penalizaciones activas.");
        }
    }

    public static void main(String[] args) {
        iniciar();
    }
}