package Vista;

import Modelo.DAOGenerico;
import Modelo.Ejemplar;
import Modelo.Libro;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

public class MenuEjemplares {

    private static Scanner scanner = new Scanner(System.in);

    public static void mostrarMenu(EntityManager em) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Ejmeplares ===");
            System.out.println("1. Crear Ejemplar");
            System.out.println("2. Ver Ejemplares");
            System.out.println("3. Actualizar Ejemplar");
            System.out.println("4. Eliminar Ejemplar");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    crearEjemplar(em);
                    break;
                case 2:
                    verEjemplares(em);
                    break;
                case 3:
                    actualizarEjemplar(em);
                    break;
                case 4:
                    eliminarEjemplar(em);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (true);
    }

    private static void crearEjemplar(EntityManager em) {
        System.out.print("Ingrese el ISBN del libro asociado: ");
        String isbn = scanner.nextLine();

        DAOGenerico<Libro> daoLibro = new DAOGenerico<>(Libro.class);
        Libro libro = daoLibro.obtenerPorCampoUnico(em, "isbn", isbn);

        if (libro != null) {
            Ejemplar ejemplar = new Ejemplar();
            ejemplar.setIsbn(libro);
            ejemplar.setEstado("Disponible");

            DAOGenerico<Ejemplar> daoEjemplar = new DAOGenerico<>(Ejemplar.class);
            daoEjemplar.guardar(em, ejemplar);
            System.out.println("Ejemplar creado exitosamente.");
        } else {
            System.out.println("❌ Libro no encontrado.");
        }
    }

    private static void verEjemplares(EntityManager em) {
        DAOGenerico<Ejemplar> daoEjemplar = new DAOGenerico<>(Ejemplar.class);
        List<Ejemplar> ejemplares = daoEjemplar.obtenerTodos(em);

        if (ejemplares.isEmpty()) {
            System.out.println("No hay ejemplares registrados.");
        } else {
            for (Ejemplar ejemplar : ejemplares) {
                System.out.println("ID: " + ejemplar.getId() +
                        ", ISBN Libro: " + ejemplar.getIsbn().getIsbn() +
                        ", Estado: " + ejemplar.getEstado());
            }
        }
    }

    private static void actualizarEjemplar(EntityManager em) {
        System.out.print("Ingrese el ID del ejemplar a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        DAOGenerico<Ejemplar> daoEjemplar = new DAOGenerico<>(Ejemplar.class);
        Ejemplar ejemplar = daoEjemplar.obtenerPorId(em, id);

        if (ejemplar != null) {
            System.out.print("Ingrese el nuevo estado del ejemplar (Disponible / No disponible): ");
            String nuevoEstado = scanner.nextLine();
            ejemplar.setEstado(nuevoEstado);

            daoEjemplar.actualizar(em, ejemplar);
            System.out.println("Ejemplar actualizado exitosamente.");
        } else {
            System.out.println("❌ Ejemplar no encontrado.");
        }
    }

    private static void eliminarEjemplar(EntityManager em) {
        System.out.print("Ingrese el ID del ejemplar a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        DAOGenerico<Ejemplar> daoEjemplar = new DAOGenerico<>(Ejemplar.class);
        Ejemplar ejemplar = daoEjemplar.obtenerPorId(em, id);

        if (ejemplar != null) {
            daoEjemplar.eliminar(em, ejemplar);
            System.out.println("Ejemplar eliminado exitosamente.");
        } else {
            System.out.println("❌ Ejemplar no encontrado.");
        }
    }
}