package hotel.model;

public class Client extends User {

    public Client(String username, String password) {
        super(username, password);
    } // Llama al constructor de la clase User

    public String getRole() {
        return "CLIENT";
    } // Devuelve el tipo de cuenta
}
