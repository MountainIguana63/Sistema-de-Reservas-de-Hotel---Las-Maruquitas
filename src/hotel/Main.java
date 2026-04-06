import hotel.model.*;
import hotel.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    // Instanciamos los servicios y herramientas globalmente para que todos los métodos los vean
    private static final Scanner scanner = new Scanner(System.in);
    private static final ServicioUser servicioUser = new ServicioUser();
    private static final ServicioHabitacion servicioHabitacion = new ServicioHabitacion();
    private static final ServicioHuesped servicioHuesped = new ServicioHuesped();
    private static final ServicioReservas servicioReservas = new ServicioReservas();
    private static final GestorArchivos gestorArchivos = new GestorArchivos("datos_hotel.txt");

    // Formateador de fechas para que el usuario escriba día/mes/año
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        // Cargar datos al iniciar (Pasamos servicioUser para que recupere las cuentas)
        gestorArchivos.cargarDatos(servicioUser);

        // Creamos un admin por defecto solo si la lista está vacía
        if (servicioUser.getUsers().isEmpty()) {
            servicioUser.register("admin", "123", "123", true);
        }

        boolean ejecutando = true;
        while (ejecutando) {
            System.out.println("\n=======================================");
            System.out.println("   SISTEMA DE RESERVAS LAS MARUQUITAS  ");
            System.out.println("=======================================");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse (Crear Cuenta)");
            System.out.println("3. Salir del Sistema");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    iniciarSesion();
                    break;
                case "2":
                    registrarNuevoUsuario();
                    break;
                case "3":
                    System.out.println("Guardando y cerrando... ¡Hasta pronto!");
                    // Opcional: guardar al salir para no perder nada
                    gestorArchivos.guardarTodo(servicioReservas.getListaDeReservas(), servicioUser.getUsers());
                    ejecutando = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void iniciarSesion() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        User usuarioLogueado = servicioUser.login(user, pass);

        if (usuarioLogueado != null) {
            flujoPrincipal(usuarioLogueado);
        }
    }

    private static void registrarNuevoUsuario() {
        System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
        System.out.print("Nombre de usuario deseado: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();
        System.out.print("Confirme su contraseña: ");
        String confirm = scanner.nextLine();

        System.out.print("¿Será administrador? (s/n): ");
        boolean esAdmin = scanner.nextLine().equalsIgnoreCase("s");

        servicioUser.register(user, pass, confirm, esAdmin);
        System.out.println("RECUERDA: Debes Guardar Datos (Opción 7) como Admin para no perder esta cuenta.");
    }

    private static void flujoPrincipal(User usuarioActual) {
        boolean sesionActiva = true;
        while (sesionActiva) {
            System.out.println("\n=======================================");
            System.out.println(" MENÚ (" + usuarioActual.getRole() + "): " + usuarioActual.getUsername());
            System.out.println("=======================================");
            System.out.println("1. Consultar Disponibilidad");
            System.out.println("2. Registrar Huésped");
            System.out.println("3. Crear Reserva");
            System.out.println("4. Cancelar Reserva");

            // Opciones exclusivas para ADMIN
            if (usuarioActual.getRole().equals("ADMIN")) {
                System.out.println("--- Opciones Administrativas ---");
                System.out.println("5. Registrar Habitación");
                System.out.println("6. Ver Todas las Reservas");
                System.out.println("7. Guardar Datos en Disco");
            }

            System.out.println("0. Cerrar Sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": consultarDisponibilidad(); break;
                case "2": registrarHuespedPrompt(); break;
                case "3": crearReservaPrompt(); break;
                case "4": cancelarReservaPrompt(); break;
                case "5": if(usuarioActual.getRole().equals("ADMIN")) registrarHabitacionPrompt(); break;
                case "6": if(usuarioActual.getRole().equals("ADMIN")) servicioReservas.imprimirTodasLasReservas(); break;
                case "7":
                    if(usuarioActual.getRole().equals("ADMIN")) {
                        gestorArchivos.guardarTodo(servicioReservas.getListaDeReservas(), servicioUser.getUsers());
                    }
                    break;
                case "0": sesionActiva = false; break;
                default: System.out.println("Opción no válida.");
            }
        }
    }


    private static void consultarDisponibilidad() {
        try {
            System.out.print("Fecha Inicio (dd/mm/yyyy): ");
            LocalDate inicio = LocalDate.parse(scanner.nextLine(), formatter);
            System.out.print("Fecha Fin (dd/mm/yyyy): ");
            LocalDate fin = LocalDate.parse(scanner.nextLine(), formatter);
            servicioReservas.listarDisponibles(servicioHabitacion.obtenerHabitaciones(), inicio, fin);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido.");
        }
    }

    private static void registrarHuespedPrompt() {
        System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre: "); String nom = scanner.nextLine();
        System.out.print("Email: "); String mail = scanner.nextLine();
        System.out.print("Teléfono: "); String tel = scanner.nextLine();
        servicioHuesped.RegistrarHuesped(new Huesped(id, nom, mail, tel));
    }

    private static void crearReservaPrompt() {
        if (servicioHuesped.getListaHuesped().isEmpty() || servicioHabitacion.obtenerHabitaciones().isEmpty()) {
            System.out.println("Error: Necesita huéspedes y habitaciones primero.");
            return;
        }
        System.out.print("Días de estancia: ");
        int dias = Integer.parseInt(scanner.nextLine());
        servicioReservas.crearNuevaReserva(servicioHuesped.getListaHuesped().get(0),
                servicioHabitacion.obtenerHabitaciones().get(0),
                LocalDate.now(), LocalDate.now().plusDays(dias));
    }

    private static void cancelarReservaPrompt() {
        System.out.print("ID de reserva: ");
        int id = Integer.parseInt(scanner.nextLine());
        servicioReservas.cancelarReserva(id);
    }

    private static void registrarHabitacionPrompt() {
        System.out.print("Número: "); int num = Integer.parseInt(scanner.nextLine());
        System.out.print("Precio: "); double pre = Double.parseDouble(scanner.nextLine());
        System.out.print("¿Es Suite? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            servicioHabitacion.registrarHabitacion(new Suite(num, pre, true, true, true, true));
        } else {
            servicioHabitacion.registrarHabitacion(new HabitacionEstandar(num, pre, true, 2));
        }
    }
}
