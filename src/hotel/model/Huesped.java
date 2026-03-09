package hotel.model;

import java.util.ArrayList;
import java.util.List;

/*
 Esta clase representa a un huésped del hotel
 Aquí se guardan sus datos básicos y las habitaciones que tiene reservadas
*/
public class Huesped {

    // Características del huésped, Se declaran private para proteger los datos
    private int id;
    private String nombre;
    private String email;
    private String telefono;

    // Lista que guarda las habitaciones que el huésped ha reservado
    private List<String> HabitacionesReservadas;

    /*
     Constructor de la clase
     Se usa cuando se crea un nuevo huésped
    */
    public Huesped(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;

        // Se inicializa la lista donde se guardarán las habitaciones reservadas
        this.HabitacionesReservadas = new ArrayList<>();
    }

    /*
     Métodos que permiten acceder a los datos del huésped desde otras clases
     sin romper la encapsulación de los private
    */

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public List<String> getHabitacionesReservadas() {
        return HabitacionesReservadas;
    }

    /*
     Este método permite agregar una habitación a la lista
     de habitaciones reservadas por el huésped
    */
    public void addHabitacionesReservadas(String HabitacionNumero) {
        HabitacionesReservadas.add(HabitacionNumero);
    }

}