package hotel.model;

public class SuperAdmin extends User {

    public SuperAdmin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "SUPER_ADMIN";
    }
}