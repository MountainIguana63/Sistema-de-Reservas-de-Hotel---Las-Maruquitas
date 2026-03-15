package hotel.model;

import java.time.LocalDate;

public class Reserva {
    //Estos son los datos que guardara nuestra ficha
    private int id;
    private Huesped huesped;
    private Habitacion habitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private double costoTotal;
    private EstadoReserva estado;

    // Un contador para que cada reserva tenga un número diferente automaticamente
    private static int contadorId= 1;

    // Esto llenara la ficha
    public Reserva(Huesped huesped, Habitacion habitacion, LocalDate entrada, LocalDate salida, double costo){
        this.id= contadorId++;      // Le pone el numero actual y suma 1 para la siguiente
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.fechaEntrada = entrada;
        this.fechaSalida = salida;
        this.costoTotal= costo;
        this.estado= EstadoReserva.ACTIVA; // Por defecto empezara activa
    }

    // Lo que nos permite leer los datos de la ficha ya que son private
    public int getId() { return id; }
    public Huesped getHuesped() { return huesped; }
    public Habitacion getHabitacion() { return habitacion; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public double getCostoTotal() { return costoTotal; }
    public EstadoReserva getEstado() { return estado; }

    // para que podamos cambiar el estado (de ACTIVA a CANCELADA)
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    // Cuando se imprima la reserva se vera ordenado
    @Override
    public String toString() {
        return "Reserva #" + id + " | " + huesped.getNombre() + " en Hab. " + habitacion.getNumero();
    }
}
