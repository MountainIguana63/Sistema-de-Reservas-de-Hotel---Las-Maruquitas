package hotel.model;

import java.time.LocalDateTime; // Necesario para registrar la fecha y hora

public abstract class User {

    protected String username;
    protected String password;
    protected LocalDateTime ultimoAcceso; // Nuevo atributo para auditar

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.ultimoAcceso = null; // Al crearse, aún no ha iniciado sesión
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }

    // Este metodo se llamará cada vez que el usuario haga Login con éxito
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    // Metodo abstracto: Obliga a las clases hijas a implementar su propio rol
    public abstract String getRole();
}
