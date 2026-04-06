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

    // Lista donde se guardan todos los huéspedes registrados
    private List<Huesped> ListaHuesped;

    /*
     Inicializa la lista de huéspedes cuando se crea el servicio
    */
    public ServicioHuesped() {
        ListaHuesped = new ArrayList<>();
    }

    /*
     Método para registrar un nuevo huésped en el sistema
     Recibe informacion del Huesped y lo agrega a la lista
    */
    public void RegistrarHuesped(Huesped huesped) {
        ListaHuesped.add(huesped);
        System.out.println("Huésped registrado correctamente");
    }

    /*
     Método para eliminar un huésped
     Primero busca el huésped por su ID y antes de eliminarlo se valida que no tenga reservas activas
    */
    public void EliminarHuesped(int id) {

        Huesped HuespedEncontrado = null;

        // Se recorre la lista de huéspedes
        for (Huesped h : ListaHuesped) {

            // Si el ID coincide se encontró el huésped
            if (h.getId() == id) {

                // Validación para verificar si tiene reservas activas
                if (!h.getHabitacionesReservadas().isEmpty()) {
                    System.out.println("No se puede eliminar el huésped porque tiene reservas activas");
                    return;
                }

                HuespedEncontrado = h;
            }
        }

        // Si se encontró el huésped se elimina de la lista
        if (HuespedEncontrado != null) {
            ListaHuesped.remove(HuespedEncontrado);
            System.out.println("Huésped eliminado correctamente");
        } else {
            System.out.println("Huésped no encontrado");
        }

    }

    /*
     Método para mostrar a los huespedes registrados
     con toda su información
    */
    public void MostrarHuespedes() {

        for (Huesped h : ListaHuesped) {

            System.out.println("ID: " + h.getId());
            System.out.println("Nombre: " + h.getNombre());
            System.out.println("Email: " + h.getEmail());
            System.out.println("Teléfono: " + h.getTelefono());

        }

    }

    /*
     Método para mostrar las habitaciones reservadas por un huésped
    */
    public void showHabitacionesReservadas(int id) {

        for (Huesped h : ListaHuesped) {

            if (h.getId() == id) {

                System.out.println("Habitaciones reservadas por " + h.getNombre());

                for (String habitacion : h.getHabitacionesReservadas()) {
                    System.out.println("Habitación: " + habitacion);
                }

                return;
            }

        }

        System.out.println("Huésped no encontrado");

    }

    /*
     Este método muestra la información del huésped
     pero ocultando datos sensibles como el email y el teléfono
     Esto simula una vista para clientes
    */
    public void showInfoHuesped() {

        for (Huesped h : ListaHuesped) {

            System.out.println("Nombre: " + h.getNombre());
            System.out.println("Cantidad de habitaciones reservadas: " + h.getHabitacionesReservadas().size());
           

        }

    }
     public List<Huesped> getListaHuesped() {
        return ListaHuesped;
    }
}
