package hotel.service;

import hotel.model.*;
import java.io.*;
import java.util.List;

public class GestorArchivos {
    private String rutaArchivo;

    // El constructor recibe el nombre del archivo
    public GestorArchivos(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Metodo para guardar (Actualizado para recibir usuarios también)
    public void guardarTodo(List<Reserva> listaReservas, List<User> listaUsuarios) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Primero guardamos los usuarios
            for (User u : listaUsuarios) {
                escritor.write("USER," + u.getUsername() + "," + u.getPassword() + "," + u.getRole());
                escritor.newLine();
            }
            //Guardamos las reservas
            for (Reserva r : listaReservas) {
                // Escribimos los datos de la reserva separados por comas
                escritor.write("RESERVA," + r.getId() + "," +
                        r.getHuesped().getNombre() + "," +
                        r.getHabitacion().getNumero() + "," +
                        r.getFechaEntrada() + "," +
                        r.getFechaSalida() + "," +
                        r.getCostoTotal() + "," +
                        r.getEstado());
                escritor.newLine();
            }
            System.out.println(">>> Sistema: Datos guardados en el disco con exito");
        } catch (IOException e) {
            System.out.println("Error al intentar guardar el archivo: " + e.getMessage());
        }
    }

    // Metodo para cargar (Actualizado para procesar usuarios)
    public void cargarDatos(ServicioUser sUser) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Aviso: No se encontro un archivo previo. Iniciando sistema limpio.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            System.out.println("--- Cargando información historica del hotel ---");
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");

                // Si la linea es un usuario, lo volvemos a registrar en el sistema
                if (datos[0].equals("USER")) {
                    // datos[1] es el usuario, datos[2] la clave, datos[3] el rol
                    boolean esAdmin = datos[3].equals("ADMIN");
                    sUser.register(datos[1], datos[2], datos[2], esAdmin);
                } else if (datos[0].equals("RESERVA")) {
                    System.out.println("Registro recuperado: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de datos: " + e.getMessage());
        }
    }
}
