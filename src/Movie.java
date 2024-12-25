import java.io.*;
import java.util.*;

/**
 * Clase que representa una película con un título.
 */
public class Movie {
    private String title;

    public Movie(String title) {
        this.title = title.trim().toLowerCase(); // Normalizamos a minúsculas y eliminamos espacios innecesarios.
    }

    public String getTitle() {
        return title;
    }

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
