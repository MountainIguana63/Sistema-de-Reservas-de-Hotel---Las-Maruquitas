package hotel.model;
/*
Esta es abstract por que como queremos tipos de habitaciones
especificos (estandar o suite).
 */

public abstract class Habitacion { // atributos comunes de las habitaciones

    protected int numero;
    protected double precio;
    protected boolean disponible;

    public Habitacion(int numero, double precio, boolean disponible){//Constructor para crear el tipo de habitacion
        this.numero = numero;
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getNumero() { // Devuelve el numero de la habitacion

        return numero;
    }

    public double getPrecio() { // Precio de la habitacion

        return precio;
    }

    public boolean isDisponible() { // Disponibilidad de la habitacion  o no (true o false)

        return disponible;
    }

    public void setDisponible(boolean disponible){ // Permite cambiar la disponibilidad de la habitacion

        this.disponible = disponible;
    }
}
