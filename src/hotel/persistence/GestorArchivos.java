package hotel.persistence;

import com.google.gson.*;
import hotel.model.*; // Necesario para que Gson conozca las clases hijas
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    private final Gson gson;
    private final String folder = "data/";

    public GestorArchivos() {
        // 1. Traductor para LocalDateTime
        JsonSerializer<LocalDateTime> dateSerializer = (src, typeOfSrc, context) ->
                new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        JsonDeserializer<LocalDateTime> dateDeserializer = (json, typeOfT, context) ->
                LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 2. Traductor Polimórfico para USER
        JsonSerializer<User> userSerializer = (src, typeOfSrc, context) -> {
            JsonObject jsonObject = context.serialize(src, src.getClass()).getAsJsonObject();
            jsonObject.addProperty("type", src.getClass().getSimpleName()); // Guardamos qué tipo de hijo es
            return jsonObject;
        };
        JsonDeserializer<User> userDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "SuperAdmin";
            switch (type) {
                case "Admin": return context.deserialize(jsonObject, Admin.class);
                case "Recepcionista": return context.deserialize(jsonObject, Recepcionista.class);
                default: return context.deserialize(jsonObject, SuperAdmin.class);
            }
        };

        // 3. Traductor Polimórfico para HABITACION
        JsonSerializer<Habitacion> habSerializer = (src, typeOfSrc, context) -> {
            JsonObject jsonObject = context.serialize(src, src.getClass()).getAsJsonObject();
            jsonObject.addProperty("type", src.getClass().getSimpleName());
            return jsonObject;
        };
        JsonDeserializer<Habitacion> habDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "HabitacionEstandar";
            switch (type) {
                case "Suite": return context.deserialize(jsonObject, Suite.class);
                default: return context.deserialize(jsonObject, HabitacionEstandar.class);
            }
        };

        // 4. Ensamblamos Gson con todos los traductores
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, dateSerializer)
                .registerTypeAdapter(LocalDateTime.class, dateDeserializer)
                .registerTypeAdapter(User.class, userSerializer)
                .registerTypeAdapter(User.class, userDeserializer)
                .registerTypeAdapter(Habitacion.class, habSerializer)
                .registerTypeAdapter(Habitacion.class, habDeserializer)
                .create();
    }

    public <T> void guardarArchivo(List<T> lista, String nombreArchivo) {
        try (Writer writer = new FileWriter(folder + nombreArchivo)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en " + nombreArchivo + ": " + e.getMessage());
        }
    }

    public <T> List<T> cargarArchivo(String nombreArchivo, Type tipoDeLista) {
        File file = new File(folder + nombreArchivo);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, tipoDeLista);
        } catch (IOException e) {
            System.err.println("Error al cargar " + nombreArchivo + ": " + e.getMessage());
            return new ArrayList<>();
        } catch (JsonSyntaxException e) {
            // Esto evita que crashee si los .json que creaste manualmente están completamente en blanco
            System.out.println("Nota: " + nombreArchivo + " está vacío. Se inicializará desde cero.");
            return new ArrayList<>();
        }
    }
}