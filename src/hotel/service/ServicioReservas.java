package hotel.service;

import hotel.model.*; // traemos todo de la carpeta model
import java.time.LocalDate; // Permite usar el formato de fechas
import java.time.temporal.ChronoUnit; // para contar los días
import java.util.ArrayList; // para guardar todas las reservas que vayamos creando
import java.util.List;

public class ServicioReservas {
    // lista para guardar todas las "fichas" (Reservas) que hagamos
    private List<Reserva> listaDeReservas;

    public ServicioReservas() {
        this.listaDeReservas = new ArrayList<>();
    }

    // Metodo para crear las reservas
    public void crearNuevaReserva(Huesped huesped, Habitacion habitacion, LocalDate entrada, LocalDate salida) {

        // Es para validar las fechas y que no se pueda salir antes de entrar
        if (!salida.isAfter(entrada)) {
            System.out.println("Error: la fecha de salida esta mal");
            return; // Se sale y no hace nada
        }

        //Validar si la habitación ya esta o estara ocupada
        if (!estaDisponible(habitacion, entrada, salida)) {
            System.out.println("Error: La habitación ya tiene una reserva en esas fechas");
            return;
        }

        // Calcular cuanto va a pagar
        // Contamos cuantos días hay entre entrada y salida
        long dias= ChronoUnit.DAYS.between(entrada, salida);
        double precioTotal= dias * habitacion.getPrecio();

        System.out.println("El costo total será: $" + precioTotal); // Mostrar el total antes de confirmar

        // Si todo esta bien creamos la ficha(Reserva)
        Reserva miReserva = new Reserva(huesped, habitacion, entrada, salida, precioTotal);
        listaDeReservas.add(miReserva); //La guardamos en nuestra lista
        System.out.println("Reserva guardada con exito por $" + precioTotal);
    }

    // Metodo para revisar disponibilidad
    public void listarDisponibles(List<Habitacion> todasLasHabitaciones, LocalDate inicio, LocalDate fin) {
        System.out.println("\n--- BUSCANDO DISPONIBILIDAD: " + inicio + " al " + fin + " ---");
        boolean hayDisponibles= false;

        for (Habitacion hab: todasLasHabitaciones){
            if (estaDisponible(hab, inicio, fin)){
                System.out.println("- Habitación #" + hab.getNumero() + " (" + hab.getClass().getSimpleName() + ")");
                hayDisponibles = true;
            }
        }

        if (!hayDisponibles){
            System.out.println("Lo sentimos, no hay habitaciones libres para esas fechas");
        }
    }

    // Logica de validación (interna)
    private boolean estaDisponible(Habitacion hab, LocalDate inicio, LocalDate fin) {
        for (Reserva r : listaDeReservas){
            if (r.getHabitacion().getNumero() == hab.getNumero() && r.getEstado() == EstadoReserva.ACTIVA) {
                if (inicio.isBefore(r.getFechaSalida()) && fin.isAfter(r.getFechaEntrada())) {
                    return false;
                }
            }
        }
        return true;
    }

    // Cancelar reserva
    public void cancelarReserva(int idReserva){
        for (Reserva r : listaDeReservas){
            if (r.getId() == idReserva){
                r.setEstado(EstadoReserva.CANCELADA);
                System.out.println("La reserva #" + idReserva + " ahora figura como CANCELADA.");
                return;
            }
        }
        System.out.println("No se encontró ninguna reserva con ese ID.");
    }

    public void imprimirTodasLasReservas(){
        if (listaDeReservas.isEmpty()){
            System.out.println("No hay historial de reservas.");
        } else {
            for (Reserva r : listaDeReservas) {
                System.out.println(r.toString());
            }
        }
    }

    // Metodo requerido para gestor de archivos
    //Permite obtener la lista de reservas para que el GestorArchivos pueda guardarlas en el archivo de texto
    public List<Reserva> getListaDeReservas() {
        return listaDeReservas;
    }
}
