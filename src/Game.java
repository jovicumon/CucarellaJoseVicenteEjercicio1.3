import java.io.IOException;
import java.util.*;

/**
 * Clase principal que gestiona el juego.
 */
public class Game {
    private List<Movie> movies;
    private Player player;
    private Movie selectedMovie;
    private Set<Character> guessedLetters;
    private int remainingAttempts;

    public Game(List<Movie> movies) {
        this.movies = movies;
        this.guessedLetters = new HashSet<>();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Selección de película aleatoria
        Random random = new Random();
        selectedMovie = movies.get(random.nextInt(movies.size()));

        // Introducir el nickname del jugador
        System.out.println("🎯 🎯 🎯 Adivina la película 🎯 🎯 🎯");
        System.out.print("Introduce tu nickname: ");
        String nickname = scanner.nextLine();
        player = new Player(nickname);

        System.out.println("El título de la película tiene " + selectedMovie.getTitle().length() + " caracteres (incluidos espacios y signos de puntuación)");
        System.out.println("La película a adivinar es: " + selectedMovie.getHiddenTitle());

        remainingAttempts = 10; // Número de intentos

        // Ciclo principal del juego
        while (remainingAttempts > 0) {
            System.out.println("\nTurnos restantes: " + remainingAttempts);  // Mostrar los turnos restantes
            System.out.println("Puntos: " + player.getScore());  // Mostrar los puntos obtenidos
            System.out.println("Elige una opción:");
            System.out.println("[1] Adivina una letra");
            System.out.println("[2] Adivina el título de la película");
            System.out.println("[3] Salir");

            // Validar entrada para el menú
            int choice = 0;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Selecciona una opción: ");
                try {
                    choice = scanner.nextInt(); // Intentamos leer un entero
                    validInput = true;  // Si se lee un número correctamente, salimos del bucle
                } catch (InputMismatchException e) {
                    System.out.println("Entrada no válida. Por favor, ingresa un número del 1 al 3.");
                    scanner.nextLine(); // Limpiar el buffer para intentar nuevamente
                }
            }

            scanner.nextLine(); // Limpiar el buffer

            switch (choice) {
                case 1 -> guessLetter(scanner); // Adivinar letra
                case 2 -> guessTitle(scanner); // Adivinar título
                case 3 -> { // Salir
                    System.out.println("Has salido del juego.");
                    return;
                }
                default -> System.out.println("Opción no válida. Intenta nuevamente.");
            }

            if (isGameWon()) { // Verifica si el juego ha sido ganado
                System.out.println("¡Felicidades! Has adivinado la película: " + selectedMovie.getTitle());
                player.addScore(20);  // Puntuar por ganar
                break;
            }
        }

        if (remainingAttempts == 0) {
            System.out.println("Te has quedado sin intentos. La película era: " + selectedMovie.getTitle());
        }

        System.out.println("Puntuación final: " + player.getScore());
    }

    private void guessLetter(Scanner scanner) {
        System.out.print("Introduce una letra: ");
        String input = scanner.nextLine().toLowerCase();

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            System.out.println("Entrada inválida. Por favor, introduce una letra válida.");
            return;
        }

        char letter = input.charAt(0);

        if (guessedLetters.contains(letter)) {
            System.out.println("Ya has intentado esta letra antes. Intenta otra.");
            return;
        }

        guessedLetters.add(letter);

        if (selectedMovie.getTitle().contains(String.valueOf(letter))) {
            System.out.println("¡Correcto! La letra '" + letter + "' está en el título.");
            player.addScore(10);
        } else {
            System.out.println("Incorrecto. La letra '" + letter + "' no está en el título.");
            player.subtractScore(10);
            remainingAttempts--;
        }

        System.out.println("Título actual: " + getRevealedTitle());
        System.out.println("Intentos restantes: " + remainingAttempts);
    }

    private void guessTitle(Scanner scanner) {
        System.out.print("Introduce el título completo: ");
        String guessedTitle = scanner.nextLine().toLowerCase();

        if (guessedTitle.equals(selectedMovie.getTitle())) {
            System.out.println("¡Correcto! Has adivinado el título.");
            player.addScore(20);
        } else {
            System.out.println("Incorrecto. No has adivinado el título.");
            player.subtractScore(20);
        }

        remainingAttempts = 0; // Terminar el juego
    }

    private String getRevealedTitle() {
        StringBuilder revealedTitle = new StringBuilder();
        for (char c : selectedMovie.getTitle().toCharArray()) {
            if (guessedLetters.contains(c) || !Character.isLetter(c)) {
                revealedTitle.append(c);
            } else {
                revealedTitle.append('*');
            }
        }
        return revealedTitle.toString();
    }

    private boolean isGameWon() {
        for (char c : selectedMovie.getTitle().toCharArray()) {
            if (Character.isLetter(c) && !guessedLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String filePath = "movies.txt";  // Ruta del archivo de películas
        try {
            List<Movie> movies = Movie.loadMoviesFromFile(filePath);
            Game game = new Game(movies);
            game.start();
        } catch (IOException e) {
            System.err.println("Error al cargar las películas: " + e.getMessage());
        }
    }
}
