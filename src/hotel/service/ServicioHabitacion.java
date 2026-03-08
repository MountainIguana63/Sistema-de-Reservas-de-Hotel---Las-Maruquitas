package hotel.service;
/*
En esta clase se maneja toda la lógica relacionada con
 las habitaciones del hotel:
 - Registrar habitaciones
 - Consultarlas
 - Eliminarlas
 - Ver cuáles están disponibles
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

    public boolean registrarHabitacion(Habitacion habitacion) { //Nueva habitacion

        for (Habitacion h : habitaciones) { //revisa cada habitacion que existe
            if (h.getNumero() == habitacion.getNumero()) { //Revisa si ya existe alguna con el mismo numero
                return false; // no se va a registrar
            }
        }

        habitaciones.add(habitacion);
        return true; // Si no existe la registra (agrega a la lista)
    }

    public List<Habitacion> obtenerHabitaciones() { // Muestra las habitaciones registradas
        return habitaciones;
    }

    public boolean eliminarHabitacion(int numero) { //Elimina habitaciones

        for (Habitacion h : habitaciones) {

            if (h.getNumero() == numero) { //Busca la habitacion por su numero

                if (!h.isDisponible()) {
                    return false; // no se puede eliminar si está ocupada
                }

                habitaciones.remove(h);
                return true; // si esta disponible se elimina
            }
        }

        return false; // si no se encuentra la habitacion
    }

    public List<Habitacion> obtenerHabitacionesDisponibles() { //Muestra las habitaciones disponibles

        List<Habitacion> disponibles = new ArrayList<>();//Lista donde guardan las habitaciones disponibles (temporalmente)

        for (Habitacion h : habitaciones) { //recorre las habitaciones registradas
            if (h.isDisponible()) { // verifica si esta disponible
                disponibles.add(h); // si esta disponible la agrega a la lista de disponibles
            }
        }

        return disponibles; // devuelve la lista de habitaciones DISPONIBLES
    }
}

