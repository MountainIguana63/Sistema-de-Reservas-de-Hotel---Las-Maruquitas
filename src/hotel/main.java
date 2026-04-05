package hotel;

import com.google.gson.reflect.TypeToken;
import hotel.model.*;
import hotel.persistence.GestorArchivos;
import hotel.service.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Instanciamos los servicios y herramientas globalmente para que todos los métodos los vean
    private static final Scanner scanner = new Scanner(System.in);
    private static final GestorArchivos gestorArchivos = new GestorArchivos();
    private static final ServicioUser servicioUser = new ServicioUser();
    private static final ServicioHabitacion servicioHabitacion = new ServicioHabitacion();
    private static final ServicioHuesped servicioHuesped = new ServicioHuesped();
    private static final ServicioReservas servicioReservas = new ServicioReservas();

    // Formateador de fechas para que el usuario escriba fácil: dia/mes/año hora:minuto
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        cargarDatos();

        boolean ejecutando = true;
        while (ejecutando) {
            System.out.println("\n=======================================");
            System.out.println("   Sistema super pro de registro de hotel  ");
            System.out.println("=======================================");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Salir del Sistema");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    iniciarSesion();
                    break;
                case "2":
                    guardarDatos();
                    System.out.println("¡Gracias por utilizar el sistema! Hasta pronto.");
                    ejecutando = false;
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente de nuevo.");
            }
        }
    }

    // ==========================================
    // CARGA Y GUARDADO DE DATOS (HU-07)
    // ==========================================
    private static void cargarDatos() {
        System.out.println("Cargando base de datos...");

        Type tipoUsers = new TypeToken<ArrayList<User>>(){}.getType();
        servicioUser.setUsers(gestorArchivos.cargarArchivo("usuarios.json", tipoUsers));

        Type tipoHabs = new TypeToken<ArrayList<Habitacion>>(){}.getType();
        servicioHabitacion.setHabitaciones(gestorArchivos.cargarArchivo("habitaciones.json", tipoHabs));

        Type tipoHuespedes = new TypeToken<ArrayList<Huesped>>(){}.getType();
        servicioHuesped.setHuespedes(gestorArchivos.cargarArchivo("huespedes.json", tipoHuespedes));

        Type tipoReservas = new TypeToken<ArrayList<Reserva>>(){}.getType();
        servicioReservas.setReservas(gestorArchivos.cargarArchivo("reservas.json", tipoReservas));

        System.out.println("✅ Datos cargados correctamente.");
    }

    private static void guardarDatos() {
        System.out.println("Guardando información...");
        gestorArchivos.guardarArchivo(servicioUser.getUsers(), "usuarios.json");
        gestorArchivos.guardarArchivo(servicioHabitacion.obtenerHabitaciones(), "habitaciones.json");
        gestorArchivos.guardarArchivo(servicioHuesped.getHuespedes(), "huespedes.json");
        gestorArchivos.guardarArchivo(servicioReservas.getReservas(), "reservas.json");
        System.out.println("💾 ¡Datos guardados con éxito!");
    }

    // ==========================================
    // SISTEMA DE LOGIN Y MENÚS
    // ==========================================
    private static void iniciarSesion() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        User usuarioLogueado = servicioUser.login(user, pass);

        if (usuarioLogueado != null) {
            System.out.println("✅ ¡Bienvenido, " + usuarioLogueado.getUsername() + "!");
            flujoPrincipal(usuarioLogueado);
        }
    }

    private static void flujoPrincipal(User usuarioActual) {
        boolean sesionActiva = true;
        while (sesionActiva) {
            System.out.println("\n=======================================");
            System.out.println(" 👤 MENÚ PRINCIPAL (" + usuarioActual.getRole() + ")");
            System.out.println("=======================================");
            System.out.println("1. Ver Habitaciones Disponibles por Fecha");
            System.out.println("2. Registrar Huésped");
            System.out.println("3. Ver Lista de Huéspedes");
            System.out.println("4. Crear Reserva");
            System.out.println("5. Cancelar Reserva");

            // Opciones dinámicas para Admins
            if (usuarioActual.getRole().equals("ADMIN") || usuarioActual.getRole().equals("SUPER_ADMIN")) {
                System.out.println("--- Opciones de Administración ---");
                System.out.println("6. Registrar Habitación");
                System.out.println("7. Eliminar Habitación");
                System.out.println("8. Eliminar Huésped");
                System.out.println("9. Ver Todas las Reservas");
            }
            // Opción exclusiva SuperAdmin
            if (usuarioActual.getRole().equals("SUPER_ADMIN")) {
                System.out.println("--- Opciones de Super Admin ---");
                System.out.println("10. Gestión de Cuentas del Sistema");
            }

            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": consultarDisponibilidad(); break;
                case "2": registrarHuesped(); break;
                case "3": servicioHuesped.mostrarHuespedes(); break;
                case "4": crearReservaPrompt(); break;
                case "5": cancelarReservaPrompt(); break;

                case "6": if (esAdmin(usuarioActual)) registrarHabitacionPrompt(); break;
                case "7": if (esAdmin(usuarioActual)) eliminarHabitacionPrompt(); break;
                case "8": if (esAdmin(usuarioActual)) eliminarHuespedPrompt(); break;
                case "9": if (esAdmin(usuarioActual)) servicioReservas.imprimirTodasLasReservas(); break;

                case "10": if (usuarioActual.getRole().equals("SUPER_ADMIN")) menuGestionCuentas(); break;

                case "0":
                    System.out.println("Cerrando sesión...");
                    guardarDatos(); // Guardar al cerrar sesión por seguridad
                    sesionActiva = false;
                    break;
                default:
                    System.out.println("❌ Opción no válida o sin permisos.");
            }
        }
    }

    private static boolean esAdmin(User u) {
        return u.getRole().equals("ADMIN") || u.getRole().equals("SUPER_ADMIN");
    }

    // ==========================================
    // LÓGICA DE NEGOCIO (Pantallas)
    // ==========================================

    private static void registrarHuesped() {
        System.out.println("\n--- REGISTRO DE HUÉSPED ---");
        System.out.print("Nombres: ");
        String nombres = scanner.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();
        System.out.print("DUI (Ej. 01234567-8): ");
        String dui = scanner.nextLine();

        System.out.print("¿Es menor de edad? (s/n): ");
        String esMenor = scanner.nextLine();

        if (esMenor.equalsIgnoreCase("s")) {
            System.out.print("DUI del Responsable/Tutor: ");
            String duiResponsable = scanner.nextLine();
            Huesped h = new Huesped(nombres, apellidos, dui, duiResponsable);
            servicioHuesped.registrarHuesped(h);
        } else {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();
            Huesped h = new Huesped(nombres, apellidos, dui, email, telefono);
            servicioHuesped.registrarHuesped(h);
        }
    }

    private static void consultarDisponibilidad() {
        try {
            System.out.println("\n--- CONSULTAR DISPONIBILIDAD ---");
            System.out.print("Fecha de Entrada (dd/MM/yyyy HH:mm): ");
            LocalDateTime entrada = LocalDateTime.parse(scanner.nextLine(), formatter);
            System.out.print("Fecha de Salida (dd/MM/yyyy HH:mm): ");
            LocalDateTime salida = LocalDateTime.parse(scanner.nextLine(), formatter);

            List<Habitacion> libres = servicioReservas.obtenerHabitacionesDisponibles(servicioHabitacion.obtenerHabitaciones(), entrada, salida);

            System.out.println("\nHabitaciones Libres en ese rango:");
            if (libres.isEmpty()) {
                System.out.println("No hay habitaciones disponibles.");
            } else {
                for (Habitacion h : libres) {
                    System.out.println("Hab #" + h.getNumero() + " | Capacidad: " + h.getCapacidadPersonas() + " | Precio/Noche: $" + h.getPrecio());
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("❌ Error: Formato de fecha incorrecto. Use dd/MM/yyyy HH:mm");
        }
    }

    private static void crearReservaPrompt() {
        try {
            System.out.println("\n--- CREAR NUEVA RESERVA ---");
            System.out.print("DUI del Titular: ");
            String duiTitular = scanner.nextLine();
            Huesped titular = servicioHuesped.buscarPorDui(duiTitular);

            if (titular == null) {
                System.out.println("❌ Huésped no encontrado. Por favor, regístrelo primero.");
                return;
            }

            System.out.print("Número de Habitación a reservar: ");
            int numHab = Integer.parseInt(scanner.nextLine());
            Habitacion hab = servicioHabitacion.buscarHabitacion(numHab);

            if (hab == null) {
                System.out.println("❌ Habitación no existe.");
                return;
            }

            System.out.print("Fecha de Entrada (dd/MM/yyyy HH:mm): ");
            LocalDateTime entrada = LocalDateTime.parse(scanner.nextLine(), formatter);
            System.out.print("Fecha de Salida (dd/MM/yyyy HH:mm): ");
            LocalDateTime salida = LocalDateTime.parse(scanner.nextLine(), formatter);

            // Acompañantes
            List<Huesped> acompanantes = new ArrayList<>();
            System.out.print("¿Llevará acompañantes? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                boolean agregando = true;
                while (agregando) {
                    System.out.print("DUI del acompañante (o escriba 'fin' para terminar): ");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("fin")) {
                        agregando = false;
                    } else {
                        Huesped acomp = servicioHuesped.buscarPorDui(input);
                        if (acomp != null) {
                            acompanantes.add(acomp);
                            System.out.println("✅ Acompañante agregado.");
                        } else {
                            System.out.println("❌ Acompañante no encontrado, regístrelo primero en el menú principal.");
                        }
                    }
                }
            }

            // Ejecutamos la lógica del servicio
            servicioReservas.crearReserva(titular, hab, entrada, salida, acompanantes);

        } catch (Exception e) {
            System.out.println("❌ Error en los datos ingresados: " + e.getMessage());
        }
    }

    private static void cancelarReservaPrompt() {
        System.out.print("\nIngrese el ID de la reserva a cancelar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            servicioReservas.cancelarReserva(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
    }

    // ==========================================
    // MENÚ DE GESTIÓN DE CUENTAS (Super Admin)
    // ==========================================
    private static void menuGestionCuentas() {
        boolean enMenu = true;
        while (enMenu) {
            System.out.println("\n--- GESTIÓN DE CUENTAS (SUPER ADMIN) ---");
            System.out.println("1. Ver todas las cuentas y último acceso");
            System.out.println("2. Registrar nueva cuenta");
            System.out.println("3. Eliminar cuenta");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    servicioUser.mostrarTodasLasCuentas();
                    break;
                case "2":
                    System.out.print("Nuevo Username: ");
                    String nUser = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String nPass = scanner.nextLine();
                    System.out.print("Confirmar Contraseña: ");
                    String cPass = scanner.nextLine();
                    System.out.println("Roles: 1(SuperAdmin) | 2(Admin) | 3(Recepcionista)");
                    System.out.print("Tipo: ");
                    int tipo = Integer.parseInt(scanner.nextLine());
                    servicioUser.registrarUsuario(nUser, nPass, cPass, tipo);
                    break;
                case "3":
                    System.out.print("Username a eliminar: ");
                    String delUser = scanner.nextLine();
                    servicioUser.eliminarUser(delUser);
                    break;
                case "0":
                    enMenu = false;
                    break;
                default:
                    System.out.println("❌ Opción inválida.");
            }
        }
    }

    // ==========================================
    // UTILIDADES ADMIN (Habitaciones)
    // ==========================================
    private static void registrarHabitacionPrompt() {
        try {
            System.out.println("\n--- REGISTRAR HABITACIÓN ---");
            System.out.print("Número: ");
            int numero = Integer.parseInt(scanner.nextLine());
            System.out.print("Precio por noche: $");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Capacidad (Personas): ");
            int cap = Integer.parseInt(scanner.nextLine());
            System.out.println("Tipo: 1. Estándar | 2. Suite");
            String tipo = scanner.nextLine();

            if (tipo.equals("1")) {
                servicioHabitacion.registrarHabitacion(new HabitacionEstandar(numero, precio, true, cap));
            } else if (tipo.equals("2")) {
                System.out.print("¿Tiene jacuzzi? (true/false): ");
                boolean j = Boolean.parseBoolean(scanner.nextLine());
                System.out.print("¿Tiene balcón? (true/false): ");
                boolean b = Boolean.parseBoolean(scanner.nextLine());
                System.out.print("¿Tiene minibar? (true/false): ");
                boolean m = Boolean.parseBoolean(scanner.nextLine());
                servicioHabitacion.registrarHabitacion(new Suite(numero, precio, true, j, b, m, cap));
            }
        } catch (Exception e) {
            System.out.println("❌ Error al ingresar los datos.");
        }
    }

    private static void eliminarHabitacionPrompt() {
        System.out.print("Número de habitación a eliminar: ");
        try {
            int num = Integer.parseInt(scanner.nextLine());
            servicioHabitacion.eliminarHabitacion(num);
        } catch (NumberFormatException e) {
            System.out.println("❌ Número inválido.");
        }
    }

    private static void eliminarHuespedPrompt() {
        System.out.print("DUI del huésped a eliminar: ");
        String dui = scanner.nextLine();
        servicioHuesped.eliminarHuesped(dui);
    }
}