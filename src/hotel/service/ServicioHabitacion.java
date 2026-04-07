package hotel.service;
/*
En esta clase se maneja toda la lógica relacionada con
 las habitaciones del hotel:
 - Registrar habitaciones
 - Consultarlas
 - Eliminarlas
 - lista general
*/
import hotel.model.Habitacion;
/*
Se importan List y ArrayList porque se necesita una estructura
donde guardar todas las habitaciones registradas en el sistema.

List permite trabajar con una colección de objetos.

ArrayList permite agregar, eliminar y recorrer elementos fácilmente durante la ejecución del programa.
*/
import java.util.ArrayList;
import java.util.List;

public class ServicioHabitacion {

    private List<Habitacion> habitaciones;
    //Se almacenan todas las habitaciones registradas
    /*
    Se usa List<Habitacion> para poder guardar cualquier tipo de habitación
    que herede de la clase Habitacion
     */
    public ServicioHabitacion() {
        habitaciones = new ArrayList<>();//Se crea un ArrayList vacio donde se va a guardar las habitaciones
    }

    // Método para inyectar datos desde el JSON
    public void setHabitaciones(List<Habitacion> habitacionesCargadas) {
        this.habitaciones = habitacionesCargadas;
    }

    public List<Habitacion> obtenerHabitaciones() {
        return habitaciones;
    }

    // HU-01: Registrar habitación validando duplicados
    public boolean registrarHabitacion(Habitacion habitacion) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == habitacion.getNumero()) {
                System.out.println("Error: Ya existe una habitación registrada con el número " + habitacion.getNumero());
                return false;
            }
        }
        habitaciones.add(habitacion);
        System.out.println("¡Habitación " + habitacion.getNumero() + " registrada con éxito!");
        return true;
    }

    // Eliminar habitación del inventario
    public boolean eliminarHabitacion(int numero) {
        // Usamos removeIf (una función Lambda de Java) que busca y elimina en una sola línea
        boolean removido = habitaciones.removeIf(h -> h.getNumero() == numero);
        if (removido) {
            System.out.println("Habitación " + numero + " eliminada del sistema.");
        } else {
            System.out.println("Error: No se encontró la habitación " + numero);
        }
        return removido;
    }

    public Habitacion buscarHabitacion(int numero) {
        for(Habitacion h : habitaciones){
            if(h.getNumero() == numero) return h;
        }
        return null;
    }
}

