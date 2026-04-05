package hotel.service;

import hotel.model.*; // traemos todo de la carpeta model
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit; // para contar los días
import java.util.ArrayList; // para guardar todas las reservas que vayamos creando
import java.util.List;

public class ServicioReservas {

    private List<Reserva> listaDeReservas;

    public ServicioReservas() {
        this.listaDeReservas = new ArrayList<>();
    }

    // Inyección de datos desde JSON y sincronización de IDs
    public void setReservas(List<Reserva> reservasCargadas) {
        this.listaDeReservas = reservasCargadas;

        int maxId = 0;
        for (Reserva r : listaDeReservas) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }

        // Sincronizamos el contador de IDs
        Reserva.setContadorId(maxId);
    }

    public List<Reserva> getReservas() {
        return listaDeReservas;
    }

    // ==========================================
    // HU-06: VALIDACIÓN DE DISPONIBILIDAD
    // ==========================================
    public boolean estaDisponible(Habitacion hab, LocalDateTime entrada, LocalDateTime salida) {
        for (Reserva r : listaDeReservas) {
            if (r.getHabitacion().getNumero() == hab.getNumero() && r.getEstado() == EstadoReserva.ACTIVA) {
                // Lógica de traslape de fechas
                boolean choque = entrada.isBefore(r.getFechaSalida()) && salida.isAfter(r.getFechaEntrada());
                if (choque) {
                    return false; // Está ocupada en esas fechas
                }
            }
        }
        return true; // Está libre
    }

    // Metodo para el Main: Filtrar qué habitaciones están libres en un rango de fechas
    public List<Habitacion> obtenerHabitacionesDisponibles(List<Habitacion> todas, LocalDateTime entrada, LocalDateTime salida) {
        List<Habitacion> libres = new ArrayList<>();
        for (Habitacion h : todas) {
            // Si la habitación no tiene problemas de estado general y pasa el filtro de fechas
            if (estaDisponible(h, entrada, salida)) {
                libres.add(h);
            }
        }
        return libres;
    }

    // ==========================================
    // HU-03: CREACIÓN DE RESERVA
    // ==========================================
    public boolean crearReserva(Huesped titular, Habitacion habitacion, LocalDateTime entrada, LocalDateTime salida, List<Huesped> acompanantes) {

        // 1. Regla de negocio: Un menor no puede ser titular
        if (titular.isEsMenor()) {
            System.out.println("Error: Un menor de edad (" + titular.getNombres() + ") no puede ser titular de la reserva.");
            return false;
        }

        // 2. Regla de fechas
        if (!salida.isAfter(entrada)) {
            System.out.println("Error: La fecha de salida debe ser posterior a la de entrada.");
            return false;
        }

        // 3. Regla de disponibilidad (HU-06)
        if (!estaDisponible(habitacion, entrada, salida)) {
            System.out.println("Error: La habitación " + habitacion.getNumero() + " ya está ocupada en esas fechas.");
            return false;
        }

        // 4. Regla de capacidad
        int totalPersonas = 1 + acompanantes.size(); // Titular + Acompañantes
        if (totalPersonas > habitacion.getCapacidadPersonas()) {
            System.out.println("Error: La cantidad de personas (" + totalPersonas + ") excede la capacidad de la habitación (" + habitacion.getCapacidadPersonas() + ").");
            return false;
        }

        // 5. Creación
        Reserva nuevaReserva = new Reserva(titular, habitacion, entrada, salida);

        // Agregamos los acompañantes a la reserva
        for (Huesped a : acompanantes) {
            nuevaReserva.agregarAcompanante(a);
        }

        // Agregamos el historial al huésped
        titular.addHabitacionesReservadas(String.valueOf(habitacion.getNumero()));

        listaDeReservas.add(nuevaReserva);
        System.out.println("¡Reserva creada exitosamente! Total a pagar: $" + nuevaReserva.getCostoTotal());
        return true;
    }

    // ==========================================
    // HU-04: CANCELACIÓN DE RESERVA
    // ==========================================
    public boolean cancelarReserva(int idReserva) {
        for (Reserva r : listaDeReservas) {
            if (r.getId() == idReserva) {
                if (r.getEstado() == EstadoReserva.CANCELADA) {
                    System.out.println("La reserva ya se encontraba cancelada.");
                    return false;
                }
                r.setEstado(EstadoReserva.CANCELADA);
                System.out.println("Reserva #" + idReserva + " ha sido CANCELADA con éxito.");
                return true;
            }
        }
        System.out.println("Error: No se encontró la reserva con ID " + idReserva);
        return false;
    }

    public void imprimirTodasLasReservas() {
        System.out.println("\n--- LISTA DE RESERVAS ---");
        if (listaDeReservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }
        for (Reserva r : listaDeReservas) {
            System.out.println(r.toString() + " | Estado: " + r.getEstado());
        }
    }
}