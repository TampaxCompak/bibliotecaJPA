package Vista;

import Controlador.ControladorPrestamo;
import Modelo.DTOPrestamo;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

public class MenuPrestamos {

    private static Scanner scanner = new Scanner(System.in);
    private static EntityManager em = Consola.getEntityManager();
    private static ControladorPrestamo controladorPrestamo = new ControladorPrestamo(em);

    public static void mostrarMenu(EntityManager em) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Préstamos ===");
            System.out.println("1. Crear Préstamo");
            System.out.println("2. Ver Préstamos");
            System.out.println("3. Eliminar Préstamo");
            System.out.println("4. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    crearPrestamo();
                    break;
                case 2:
                    verPrestamos();
                    break;
                case 3:
                    eliminarPrestamo();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (true);
    }

    private static void verPrestamos() {
        System.out.print("Ingrese el DNI del usuario: ");
        String dni = scanner.nextLine();

        List<DTOPrestamo> prestamos = controladorPrestamo.obtenerPrestamosPorUsuario(dni);

        if (prestamos == null || prestamos.isEmpty()) {
            System.out.println("No tiene préstamos registrados.");
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

    private static void crearPrestamo() {
        System.out.print("Ingrese el DNI del usuario: ");
        String dni = scanner.nextLine();
        System.out.print("Ingrese el ID del ejemplar: ");
        int idEjemplar = scanner.nextInt();
        scanner.nextLine();

        DTOPrestamo dtoPrestamo = controladorPrestamo.solicitarPrestamo(dni, idEjemplar);

        if (dtoPrestamo != null) {
            System.out.println("✅ Préstamo creado exitosamente.");
            System.out.println("Detalles del préstamo:");
            System.out.println("ID: " + dtoPrestamo.getId() +
                    ", DNI Usuario: " + dtoPrestamo.getUsuarioDni() +
                    ", Ejemplar ID: " + dtoPrestamo.getEjemplarId() +
                    ", Fecha Inicio: " + dtoPrestamo.getFechaInicio() +
                    ", Fecha Límite: " + dtoPrestamo.getFechaLimite() +
                    ", Fecha Devolución: " + dtoPrestamo.getFechaDevolucion());
        } else {
            System.out.println("❌ No se pudo crear el préstamo.");
        }
    }

    private static void eliminarPrestamo() {
        System.out.print("Ingrese el ID del préstamo a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean eliminado = controladorPrestamo.eliminarPrestamo(id);

        if (eliminado) {
            System.out.println("✅ Préstamo eliminado exitosamente.");
        } else {
            System.out.println("❌ No se pudo eliminar el préstamo.");
        }
    }
}