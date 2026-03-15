package hotel.model;

public abstract class User {

    protected String username;
    protected String password;
// Protected permite que las clases relacionadas puedan acceder a las variables username y password

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    } // Este es el constructor de la clase User

    public String getUsername() {
        return username;
    } // Obtener nombre de usuario

    public String getPassword() {
        return password;
    } // Obtener contraseña

    public String getRole() {
        return "USER";
    } // Obtener tipo de usuario
}
