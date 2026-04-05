package hotel.model;
/* Al igual que habitacionEstandar hereda la clase de Habitacion.
Posee carcateristcias adicionales.
*/
public class Suite extends Habitacion{ // Caracteristicas adicionales
    private boolean jacuzzi;
    private boolean balcon;
    private boolean minibar;

    /*
    Constructor y usa super() para inicializar los atributos de Habitacion.
     */
    public Suite(int numero, double precio, boolean disponible, boolean jacuzzi, boolean balcon, boolean minibar, int capacidadPersonas) {
        super(numero, precio, disponible, capacidadPersonas);
        this.jacuzzi = jacuzzi;
        this.balcon = balcon;
        this.minibar = minibar;
    }
    //Aqui devuelve si cuenta o no ya sea con jacuzzi,balcon y minibar
    public boolean tieneJacuzzi() {
        return jacuzzi;
    }

    public boolean tieneBalcon() {
        return balcon;
    }

    public boolean tieneMinibar() {
        return minibar;
    }
}
