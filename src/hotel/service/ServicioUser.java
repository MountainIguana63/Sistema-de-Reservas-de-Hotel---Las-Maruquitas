package hotel.service;

import hotel.model.*;
import java.util.ArrayList;
import java.util.List;

// Esta clase gestiona las operaciones relacionadas al usuario

public class ServicioUser {

    // Esta lista al almacena todos los usuarios registrados
    private List<User> users = new ArrayList<>();

    // Esto es para crear un nuevo usuario
    public void register(String username, String password, String confirmPassword, boolean admin) {

        // Esto es para verificar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        // Esto es para verificar que el usuario no exista anteriormente
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                System.out.println("El usuario ingresado ya existe.");
                return;
            }
        }

        // Crea un Admin o Client dependiendo el tipo de cuenta
        if (admin) {
            users.add(new Admin(username, password));
        } else {
            users.add(new Client(username, password));
        }
        System.out.println("El usuario se ha registrado correctamente.");
    }

    // Esto es para iniciar sesión en la plataforma
    public User login(String username, String password) {

        // Buscar usuario en la lista
        for (User u : users) {

            if (u.getUsername().equals(username)) {

                // Verificar que la contraseña sea correcta
                if (u.getPassword().equals(password)) {
                    System.out.println("Se ha registrado correctamente.");
                    return u;
                } else {
                    System.out.println("La contraseña es incorrecta.");
                    return null;
                }

            }
        }

        System.out.println("El usuario no se encuentra.");
        return null;
    }

    // Convertir usuario en admin
    public void giveAdmin(String username) {

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getUsername().equals(username)) {

                // Reemplazar usuario por admin
                users.set(i, new Admin(u.getUsername(),u.getPassword()));

                System.out.println("El usuario ahora es administrador.");
                return;
            }
        }
        System.out.println("El usuario no se encuentra.");
    }

    // Eliminar usuarios
    public void deleteUser(String username) {

        int adminCount = 0;

        // Contar cuántos administradores existen
        for (User u: users) {
            if (u.getRole().equals("ADMIN")) {
                adminCount++;
            }
        }

        for (User u: users) {
            if (u.getUsername().equals(username)) {

                // Esto es para no eliminar al único administrador
                if (u.getRole().equals("ADMIN") && adminCount == 1) {
                    System.out.println("No se puede eliminar al único administrador");
                    return;
                }
            }
        }
            System.out.println("El usuario no se encuentra.");
        }

    // Verificar el tipo de cuenta
    public void checkRole(String username) {
        for (User u: users) {

            if (u.getUsername().equals(username)) {
                System.out.println("Tipo de cuenta: " + u.getRole());
                return;
            }
        }

        System.out.println("El usuario no se encuentra.");
    }
}


