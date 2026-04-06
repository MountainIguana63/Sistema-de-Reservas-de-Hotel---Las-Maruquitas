import hotel.model.*;
import hotel.service.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicialización de todos los servicios
        ServicioHabitacion servHab= new ServicioHabitacion();
        ServicioHuesped servHues= new ServicioHuesped();
        ServicioReservas servRes= new ServicioReservas();

        // Creamos el gestor de archivos
        GestorArchivos gestor = new GestorArchivos("datos_hotel.txt");

        // Cargamos datos al iniciar (si existen)
        gestor.cargarDatos();

        Scanner lector = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n========== SISTEMA HOTELERO ==========");
            System.out.println("1. Registrar Habitación");
            System.out.println("2. Registrar Huésped");
            System.out.println("3. Consultar Disponibilidad");
            System.out.println("4. Crear Reserva");
            System.out.println("5. Cancelar Reserva");
            System.out.println("6. Guardar y Salir");
            System.out.print("Seleccione una opción: ");
            opcion = lector.nextInt();

            switch (opcion) {
                case 1: // Registrar habitación
                    System.out.print("Número de habitación: ");
                    int num = lector.nextInt();
                    System.out.print("Precio por noche: ");
                    double precio = lector.nextDouble();
                    System.out.print("¿Es Suite? (1: Sí, 2: No): ");
                    int tipo = lector.nextInt();

                    if(tipo == 1) {
                        servHab.registrarHabitacion(new Suite(num, precio, true, true, true, true));
                    } else {
                        servHab.registrarHabitacion(new HabitacionEstandar(num, precio, true, 2));
                    }
                    System.out.println("Habitación registrada.");
                    break;

                case 2: // Registrar huesped
                    System.out.print("ID Huésped: ");
                    int idH = lector.nextInt();
                    lector.nextLine(); // Limpiar buffer
                    System.out.print("Nombre: ");
                    String nom = lector.nextLine();
                    System.out.print("Email: ");
                    String mail = lector.next();
                    System.out.print("Teléfono: ");
                    String tel = lector.next();

                    servHues.RegistrarHuesped(new Huesped(idH, nom, mail, tel));
                    break;

                case 3: // Consultar disponibilidad
                    System.out.print("Fecha entrada (DD/MM/AAAA): ");
                    LocalDate f1 = LocalDate.parse(lector.next());
                    System.out.print("Fecha salida (DD/MM/AAAA): ");
                    LocalDate f2 = LocalDate.parse(lector.next());

                    servRes.listarDisponibles(servHab.obtenerHabitaciones(), f1, f2);
                    break;

                case 4: // Crear reserva
                    // Para esta prueba, usaremos el primer huésped y habitación registrados
                    if(servHues.getListaHuesped().isEmpty() || servHab.obtenerHabitaciones().isEmpty()){
                        System.out.println("Error: Debe registrar primero un huésped y una habitación.");
                    } else {
                        System.out.print("Dias de estancia: ");
                        int cantDias = lector.nextInt();
                        LocalDate hoy = LocalDate.now();
                        LocalDate fin = hoy.plusDays(cantDias);

                        // Usamos los primeros de la lista para probar
                        servRes.crearNuevaReserva(servHues.getListaHuesped().get(0),
                                servHab.obtenerHabitaciones().get(0), hoy, fin);
                    }
                    break;

                case 5: // Cancelar reserva
                    System.out.print("ID de reserva a cancelar: ");
                    int idR = lector.nextInt();
                    servRes.cancelarReserva(idR);
                    break;

                case 6: // Guardar y salir
                    System.out.println("Datos guardados");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 6);

        lector.close();
    }
}