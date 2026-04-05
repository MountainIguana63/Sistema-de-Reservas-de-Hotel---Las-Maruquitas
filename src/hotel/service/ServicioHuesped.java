package hotel.service;

import hotel.model.Huesped;
import java.util.ArrayList;
import java.util.List;

/*
 Esta clase se encarga de manejar todas las operaciones
 relacionadas con los huéspedes del sistema como
 registrar huéspedes, eliminarlos o ver sus reservas
*/
public class ServicioHuesped {

    private List<Huesped> listaHuespedes;

    public ServicioHuesped() {
        listaHuespedes = new ArrayList<>();
    }

    // Método para inyectar datos desde el JSON y sincronizar IDs
    public void setHuespedes(List<Huesped> huespedesCargados) {
        this.listaHuespedes = huespedesCargados;

        // Buscamos cuál fue el último ID usado para que el contador no se reinicie
        int maxId = 0;
        for (Huesped h : listaHuespedes) {
            if (h.getId() > maxId) {
                maxId = h.getId();
            }
        }
        Huesped.setContadorId(maxId); // Sincronizamos el contador de la clase Modelo
    }

    public List<Huesped> getHuespedes() {
        return listaHuespedes;
    }

    // HU-02: Registrar huésped validando DUI
    public boolean registrarHuesped(Huesped huesped) {
        // Validar que no exista alguien con el mismo DUI
        for (Huesped h : listaHuespedes) {
            if (h.getDui().equals(huesped.getDui())) {
                System.out.println("Error: Ya existe un huésped registrado con el DUI " + huesped.getDui());
                return false;
            }
        }
        listaHuespedes.add(huesped);
        System.out.println("¡Huésped " + huesped.getNombres() + " registrado correctamente con ID: " + huesped.getId() + "!");
        return true;
    }

    public boolean eliminarHuesped(String dui) {
        for (Huesped h : listaHuespedes) {
            if (h.getDui().equals(dui)) {
                // Validación: No se puede borrar si tiene reservas
                if (!h.getHabitacionesReservadas().isEmpty()) {
                    System.out.println("Error: No se puede eliminar al huésped porque tiene historial de reservas.");
                    return false;
                }
                listaHuespedes.remove(h);
                System.out.println("Huésped eliminado correctamente.");
                return true;
            }
        }
        System.out.println("Error: Huésped no encontrado en el sistema.");
        return false;
    }

    // Método clave para el Main: Buscar un huésped por su documento
    public Huesped buscarPorDui(String dui) {
        for (Huesped h : listaHuespedes) {
            if (h.getDui().equals(dui)) return h;
        }
        return null;
    }

    public void mostrarHuespedes() {
        System.out.println("\n--- LISTA DE HUÉSPEDES ---");
        if(listaHuespedes.isEmpty()){
            System.out.println("No hay huéspedes registrados.");
            return;
        }
        for (Huesped h : listaHuespedes) {
            System.out.println(h.toString());
        }
    }
}
