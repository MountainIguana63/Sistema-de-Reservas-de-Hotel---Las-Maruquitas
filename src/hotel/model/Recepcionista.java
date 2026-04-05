package hotel.model;

public class Recepcionista extends User {

    public Recepcionista(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "RECEPCIONISTA";
    }
}