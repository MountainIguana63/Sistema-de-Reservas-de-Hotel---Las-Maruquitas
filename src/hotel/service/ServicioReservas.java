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

    //Metodo para revisar disponibilidad
    private boolean estaDisponible(Habitacion hab, LocalDate inicio, LocalDate fin) {
        for (Reserva r: listaDeReservas) {
            // Si es la misma habitación Y la reserva esta ACTIVA
            if (r.getHabitacion().getNumero() == hab.getNumero() && r.getEstado() == EstadoReserva.ACTIVA) {
                // Logica de choque de fechas
                if (inicio.isBefore(r.getFechaSalida()) && fin.isAfter(r.getFechaEntrada())) {
                    return false; // Esta ocupada
                }
            }
        }
        return true; // Esta libre
    }

    // Metodo para cancelar (cambiar estado, no eliminar)
    public void cancelarReserva(int idReserva){
        for (Reserva r: listaDeReservas){
            if (r.getId() == idReserva) {
                r.setEstado(EstadoReserva.CANCELADA); // Cambiamos el Enum
                System.out.println("Reserva #" + idReserva + " ha sido cancelada");
                return;
            }
        }
        System.out.println("No se encontro la reserva.");
    }

    // Metodo para mostrar en pantalla todas las reservas que existen en el sistema
    public void imprimirTodasLasReservas(){
        if (listaDeReservas.isEmpty()){
            System.out.println("No hay reservas registradas.");
        } else {
            for (Reserva r : listaDeReservas) {
                System.out.println(r.toString());
            }
        }
    }
}