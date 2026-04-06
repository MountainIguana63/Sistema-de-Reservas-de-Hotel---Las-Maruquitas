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

    // Metodo para guardar
    public void guardarDatos(List<Reserva> listaReservas) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Reserva r : listaReservas) {
                // Escribimos los datos de la reserva separados por comas
                escritor.write(r.getId() + "," +
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

    // Metodo para cargar
    public void cargarDatos() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Aviso: No se encontro un archivo previo. Iniciando sistema limpio.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            System.out.println("--- Cargando información historica del hotel ---");
            while ((linea = lector.readLine()) != null) {
                System.out.println("Registro recuperado: " + linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de datos: " + e.getMessage());
        }
    }
}