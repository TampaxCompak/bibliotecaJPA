package Vista;

import Controlador.ControladorPrestamo;
import jakarta.persistence.EntityManager;
import java.util.Scanner;

public class MenuPrestamos {

    private static Scanner scanner = new Scanner(System.in);
    private static ControladorPrestamo controladorPrestamo;

    public static void mostrarMenu(EntityManager em) {
        controladorPrestamo = new ControladorPrestamo(em);
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
        controladorPrestamo.obtenerPrestamosPorUsuario(dni);
    }

    private static void crearPrestamo() {
        System.out.print("Ingrese el DNI del usuario: ");
        String dni = scanner.nextLine();
        System.out.print("Ingrese el ID del ejemplar: ");
        int idEjemplar = scanner.nextInt();
        scanner.nextLine();

        controladorPrestamo.solicitarPrestamo(dni, idEjemplar);
    }

    private static void eliminarPrestamo() {
        System.out.print("Ingrese el ID del préstamo a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        controladorPrestamo.eliminarPrestamo(id);
    }
}
