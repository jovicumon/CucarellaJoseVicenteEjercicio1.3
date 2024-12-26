import java.io.*;
import java.util.*;

/**
 * Clase que representa una película con un título.
 */
public class Movie {
    private String title;

    /**
     * Constructor que crea una instancia de la clase Movie.
     *
     * @param title El título de la película.
     */
    public Movie(String title) {
        this.title = title.trim().toLowerCase(); // Normalizamos a minúsculas y eliminamos espacios innecesarios.
    }

    /**
     * Obtiene el título de la película.
     *
     * @return El título de la película.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Obtiene una versión oculta del título, donde las letras están reemplazadas por asteriscos
     * y los caracteres no alfabéticos permanecen sin cambios.
     *
     * @return Una cadena que representa el título oculto.
     */
    public String getHiddenTitle() {
        StringBuilder hiddenTitle = new StringBuilder();
        for (char c : title.toCharArray()) {
            if (Character.isLetter(c)) {
                hiddenTitle.append('*');
            } else {
                hiddenTitle.append(c);
            }
        }
        return hiddenTitle.toString();
    }

    /**
     * Carga una lista de películas desde un archivo de texto.
     *
     * @param filePath La ruta del archivo de texto que contiene los títulos de las películas.
     * @return Una lista de objetos Movie creados a partir de los títulos en el archivo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static List<Movie> loadMoviesFromFile(String filePath) throws IOException {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    movies.add(new Movie(line));
                }
            }
        }
        return movies;
    }
}
