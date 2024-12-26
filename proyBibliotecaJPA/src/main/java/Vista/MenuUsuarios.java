package Vista;

import Modelo.DAOGenerico;
import Modelo.Usuario;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

public class MenuUsuarios {

    private static Scanner scanner = new Scanner(System.in);
    private static EntityManager em = Consola.getEntityManager();

    public static void mostrarMenu(EntityManager em) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Usuarios ===");
            System.out.println("1. Crear Usuario");
            System.out.println("2. Ver Usuarios");
            System.out.println("3. Actualizar Usuario");
            System.out.println("4. Eliminar Usuario");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    crearUsuario(em);
                    break;
                case 2:
                    verUsuarios(em);
                    break;
                case 3:
                    actualizarUsuario(em);
                    break;
                case 4:
                    eliminarUsuario(em);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (true);
    }

    private static void crearUsuario(EntityManager em) {
        System.out.print("Ingrese el DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String password = scanner.nextLine();
        System.out.print("Ingrese el tipo de usuario ('administrador' para administradores/'normal' para usuarios sin permisos): ");
        String tipo = scanner.nextLine();

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setDni(dni);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setTipo(tipo);

        DAOGenerico<Usuario> daoUsuario = new DAOGenerico<>(Usuario.class);
        daoUsuario.guardar(em, nuevoUsuario);
        System.out.println("Usuario creado exitosamente.");
    }

    private static void verUsuarios(EntityManager em) {
        DAOGenerico<Usuario> daoUsuario = new DAOGenerico<>(Usuario.class);
        List<Usuario> usuarios = daoUsuario.obtenerTodos(em);

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println("DNI: " + usuario.getDni() +
                        ", Nombre: " + usuario.getNombre() +
                        ", Tipo: " + usuario.getTipo());
            }
        }
    }

    private static void actualizarUsuario(EntityManager em) {
        System.out.print("Ingrese el DNI del usuario a actualizar: ");
        String dni = scanner.nextLine();
        DAOGenerico<Usuario> daoUsuario = new DAOGenerico<>(Usuario.class);
        Usuario usuario = daoUsuario.obtenerPorCampoUnico(em, "dni", dni);

        if (usuario != null) {
            System.out.print("Ingrese el nuevo nombre: ");
            usuario.setNombre(scanner.nextLine());
            System.out.print("Ingrese el nuevo email: ");
            usuario.setEmail(scanner.nextLine());
            System.out.print("Ingrese la nueva contraseña: ");
            usuario.setPassword(scanner.nextLine());
            System.out.print("Ingrese el nuevo tipo de usuario (administrador/normal): ");
            usuario.setTipo(scanner.nextLine());

            daoUsuario.actualizar(em, usuario);
            System.out.println("Usuario actualizado exitosamente.");
        } else {
            System.out.println("❌ Usuario no encontrado.");
        }
    }

    private static void eliminarUsuario(EntityManager em) {
        System.out.print("Ingrese el DNI del usuario a eliminar: ");
        String dni = scanner.nextLine();
        DAOGenerico<Usuario> daoUsuario = new DAOGenerico<>(Usuario.class);
        Usuario usuario = daoUsuario.obtenerPorCampoUnico(em, "dni", dni);

        if (usuario != null) {
            daoUsuario.eliminar(em, usuario);
            System.out.println("Usuario eliminado exitosamente.");
        } else {
            System.out.println("❌ Usuario no encontrado.");
        }
    }
}