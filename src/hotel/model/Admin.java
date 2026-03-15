package hotel.model;

public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    } // super() llama al constructor de la clase User

    public String getRole() {
        return "ADMIN";
    } // Identificador del tipo de usuario
}