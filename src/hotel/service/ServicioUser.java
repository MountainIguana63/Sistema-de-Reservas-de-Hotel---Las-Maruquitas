package hotel.service;

import hotel.model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ServicioUser {

    private List<User> users;

    public ServicioUser() {
        this.users = new ArrayList<>();
    }

    // Método para inyectar los datos cuando se carguen del JSON
    public void setUsers(List<User> usersCargados) {
        this.users = usersCargados;
        // Si el archivo JSON estaba vacío o no existía, creamos la cuenta maestra
        if (this.users.isEmpty()) {
            this.users.add(new SuperAdmin("admin", "poo2026"));
        }
    }

    public List<User> getUsers() {
        return users;
    }

    // ==========================================
    // LÓGICA DE LOGIN (Bug corregido)
    // ==========================================
    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                // Antes comparaba username con password, ¡ya está corregido!
                if (u.getPassword().equals(password)) {
                    u.setUltimoAcceso(LocalDateTime.now()); // Registramos a qué hora entró
                    return u;
                } else {
                    System.out.println("❌ Error: Credenciales incorrectas.");
                    return null;
                }
            }
        }
        System.out.println("Error: El usuario no existe.");
        return null;
    }

    // ==========================================
    // REGISTRO DE CUENTAS (Validaciones)
    // ==========================================
    // Tipo: 1 = SuperAdmin, 2 = Admin, 3 = Recepcionista
    public boolean registrarUsuario(String username, String password, String confirmPassword, int tipo) {

        // 1. Doble verificación de contraseña
        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Error: Credenciales incorrectas.");
            return false;
        }

        // 2. Validación de formato de usuario (Regex)
        // Solo letras (incluye tildes y ñ), entre 1 y 10 caracteres
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{1,10}$", username)) {
            System.out.println("Error: El usuario debe tener máximo 10 letras, sin espacios ni caracteres especiales.");
            return false;
        }

        // 3. Verificar si ya existe
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                System.out.println("Error: El usuario ingresado ya existe en el sistema.");
                return false;
            }
        }

        // 4. Creación según el rol y validación de regla de negocio
        if (tipo == 1) {
            // Contamos cuántos SuperAdmins hay actualmente
            long totalSuperAdmins = users.stream().filter(u -> u instanceof SuperAdmin).count();
            if (totalSuperAdmins >= 2) {
                System.out.println("Error: Ya existen 2 Super Admins. No se pueden crear más.");
                return false;
            }
            users.add(new SuperAdmin(username, password));

        } else if (tipo == 2) {
            users.add(new Admin(username, password));
        } else {
            users.add(new Recepcionista(username, password));
        }

        System.out.println("¡Usuario '" + username + "' registrado exitosamente!");
        return true;
    }

    // ==========================================
    // GESTIÓN DE CUENTAS
    // ==========================================
    public boolean eliminarUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                // Bloqueo: No puedes eliminar a un SuperAdmin si es el único que queda
                if (u instanceof SuperAdmin) {
                    long totalSuperAdmins = users.stream().filter(user -> user instanceof SuperAdmin).count();
                    if (totalSuperAdmins <= 1) {
                        System.out.println("Error: No puedes eliminar la única cuenta Super Admin.");
                        return false;
                    }
                }
                users.remove(u);
                System.out.println("Usuario '" + username + "' eliminado.");
                return true;
            }
        }
        System.out.println("Error: El usuario no se encuentra.");
        return false;
    }

    public void mostrarTodasLasCuentas() {
        System.out.println("\n--- LISTA DE USUARIOS ---");
        for (User u : users) {
            String acceso = (u.getUltimoAcceso() != null) ? u.getUltimoAcceso().toString() : "Nunca ha iniciado sesión";
            System.out.println("- " + u.getUsername() + " | Rol: " + u.getRole() + " | Último acceso: " + acceso);
        }
    }
}
