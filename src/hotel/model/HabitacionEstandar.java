package hotel.model;
/*
Hereda la clase Habitacion, es decir ya tiene número, precio y disponible.
Asi que solo se le agrega información específica.
*/

public class HabitacionEstandar extends Habitacion{

    private int capacidadPersonas; //Cantidad max de personas

    /*
    Aqui esta el constructor y se usa super() para llamar al constructor de la
    clase padre, es decir, el constructor de habitación, y asi inicializar los atributos que vienen de esa clase
     */
    public HabitacionEstandar(int numero, double precio, boolean disponible, int capacidadPersonas) {
        super(numero, precio, disponible);
        this.capacidadPersonas = capacidadPersonas;
    }

    public int getCapacidadPersonas(){ // Devuelve la cantidad max.
        return capacidadPersonas;
    }
}
