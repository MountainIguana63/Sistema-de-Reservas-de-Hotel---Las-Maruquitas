package hotel.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int id;
    private Huesped titular; // El que paga
    private List<Huesped> acompanantes; // Lista de acompañantes por DUI
    private Habitacion habitacion;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private double costoTotal;
    private EstadoReserva estado;

    private static int contadorId = 1;

    public Reserva(Huesped titular, Habitacion habitacion, LocalDateTime entrada, LocalDateTime salida) {
        this.id = contadorId++;
        this.titular = titular;
        this.acompanantes = new ArrayList<>();
        this.habitacion = habitacion;
        this.fechaEntrada = entrada;
        this.fechaSalida = salida;
        this.estado = EstadoReserva.ACTIVA;
        // El costo se calcula automáticamente al momento de crear la reserva
        this.costoTotal = calcularCostoTotal();
    }

    // HU-05: El cálculo debe realizarse mediante métodos del modelo
    public double calcularCostoTotal() {
        long horas = ChronoUnit.HOURS.between(fechaEntrada, fechaSalida);
        // Calculamos días (mínimo 1 día si la estancia es corta)
        double dias = Math.max(1, Math.ceil(horas / 24.0));
        return dias * habitacion.getPrecio();
    }

    // Método para agregar acompañantes validando la capacidad de la habitación
    public boolean agregarAcompanante(Huesped acompanante) {
        // El titular + acompañantes actuales + el nuevo no debe superar la capacidad
        if ((1 + acompanantes.size() + 1) <= habitacion.getCapacidadPersonas()) {
            acompanantes.add(acompanante);
            return true;
        }
        return false; // Habitación llena
    }

    // Getters
    public int getId() { return id; }
    public Huesped getTitular() { return titular; }
    public List<Huesped> getAcompanantes() { return acompanantes; }
    public Habitacion getHabitacion() { return habitacion; }
    public LocalDateTime getFechaEntrada() { return fechaEntrada; }
    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public double getCostoTotal() { return costoTotal; }
    public EstadoReserva getEstado() { return estado; }

    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    // Metodo para sincronizar el ID con el JSON
    public static void setContadorId(int maxId) {
        contadorId = maxId + 1;
    }

    @Override
    public String toString() {
        return "Reserva #" + id + " | Titular: " + titular.getNombres() +
                " | Hab: " + habitacion.getNumero() + " | Total: $" + costoTotal;
    }
}