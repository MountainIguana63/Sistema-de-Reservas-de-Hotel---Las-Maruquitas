package hotel.model;

public abstract class Habitacion {

    protected int numero;
    protected double precio;
    protected boolean disponible;

    public Habitacion(int numero, double precio, boolean disponible) {
        this.numero = numero;
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getNumero() {
        return numero;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible){
        this.disponible = disponible;
    }
}
