package Vista;

import Modelo.DAOGenerico;
import Modelo.Libro;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

public class MenuLibros {

    private static Scanner scanner = new Scanner(System.in);
    private static EntityManager em = Consola.getEntityManager();

    public static void mostrarMenu(EntityManager em) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Libros ===");
            System.out.println("1. Crear Libro");
            System.out.println("2. Ver Libros");
            System.out.println("3. Actualizar Libro");
            System.out.println("4. Eliminar Libro");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    crearLibro(em);
                    break;
                case 2:
                    verLibros(em);
                    break;
                case 3:
                    actualizarLibro(em);
                    break;
                case 4:
                    eliminarLibro(em);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (true);
    }

    private static void crearLibro(EntityManager em) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese el autor: ");
        String autor = scanner.nextLine();
        System.out.print("Ingrese el ISBN: ");
        String isbn = scanner.nextLine();

        Libro nuevoLibro = new Libro();
        nuevoLibro.setTitulo(titulo);
        nuevoLibro.setAutor(autor);
        nuevoLibro.setIsbn(isbn);

        DAOGenerico<Libro> daoLibro = new DAOGenerico<>(Libro.class);
        daoLibro.guardar(em, nuevoLibro);
        System.out.println("Libro creado exitosamente.");
    }

    private static void verLibros(EntityManager em) {
        DAOGenerico<Libro> daoLibro = new DAOGenerico<>(Libro.class);
        List<Libro> libros = daoLibro.obtenerTodos(em);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro libro : libros) {
                System.out.println("Título: " + libro.getTitulo() +
                        ", Autor: " + libro.getAutor() +
                        ", ISBN: " + libro.getIsbn());
            }
        }
    }

    private static void actualizarLibro(EntityManager em) {
        System.out.print("Ingrese el ISBN del libro a actualizar: ");
        String isbn = scanner.nextLine();
        DAOGenerico<Libro> daoLibro = new DAOGenerico<>(Libro.class);
        Libro libro = daoLibro.obtenerPorCampoUnico(em, "isbn", isbn);

        if (libro != null) {
            System.out.print("Ingrese el nuevo título: ");
            libro.setTitulo(scanner.nextLine());
            System.out.print("Ingrese el nuevo autor: ");
            libro.setAutor(scanner.nextLine());

            daoLibro.actualizar(em, libro);
            System.out.println("Libro actualizado exitosamente.");
        } else {
            System.out.println("❌ Libro no encontrado.");
        }
    }

    private static void eliminarLibro(EntityManager em) {
        System.out.print("Ingrese el ISBN del libro a eliminar: ");
        String isbn = scanner.nextLine();
        DAOGenerico<Libro> daoLibro = new DAOGenerico<>(Libro.class);
        Libro libro = daoLibro.obtenerPorCampoUnico(em, "isbn", isbn);

        if (libro != null) {
            daoLibro.eliminar(em, libro);
            System.out.println("Libro eliminado exitosamente.");
        } else {
            System.out.println("❌ Libro no encontrado.");
        }
    }
}