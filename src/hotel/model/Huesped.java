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
    private String nombres;
    private String apellidos;
    private String dui;
    private String email;
    private String telefono;

    // Atributos nuevos para manejar menores de edad
    private boolean esMenor;
    private String duiResponsable;

    // Lista que guarda las habitaciones que el huésped ha reservado
    private List<String> HabitacionesReservadas;

    // Generador automático de IDs
    private static int contadorId = 1;

    // 1. Constructor para Huésped Mayor de Edad (Estándar)
    public Huesped(String nombres, String apellidos, String dui, String email, String telefono) {
        this.id = contadorId++;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dui = dui;
        this.email = email;
        this.telefono = telefono;
        this.esMenor = false;
        this.duiResponsable = "N/A"; // No aplica
        this.HabitacionesReservadas = new ArrayList<>();
    }

    // 2. Constructor para Huésped Menor de Edad
    public Huesped(String nombres, String apellidos, String dui, String duiResponsable) {
        this.id = contadorId++;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dui = dui;
        this.email = "No requerido (Menor)";
        this.telefono = "No requerido (Menor)";
        this.esMenor = true;
        this.duiResponsable = duiResponsable;
        this.HabitacionesReservadas = new ArrayList<>();
    }

    /*
     Métodos que permiten acceder a los datos del huésped desde otras clases
     sin romper la encapsulación de los private
    */

    public int getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDui() {
        return dui;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isEsMenor() {
        return esMenor;
    }

    public String getDuiResponsable() {
        return duiResponsable;
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

    // Método para sincronizar el contador de IDs al cargar el JSON
    public static void setContadorId(int maxId) {
        contadorId = maxId + 1;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | " + nombres + " " + apellidos + " | DUI: " + dui;
    }

}