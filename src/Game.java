import java.io.*;
import java.util.*;

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

        System.out.println("Bienvenido al juego de adivinar la película!");
        System.out.print("Introduce tu nickname: ");
        String nickname = scanner.nextLine();
        player = new Player(nickname, 0);

        System.out.println("Título de la película: " + selectedMovie.getHiddenTitle());

        remainingAttempts = 10; // Asignamos el valor directamente

        // Ciclo principal del juego
        while (remainingAttempts > 0) {
            System.out.println("\nOpciones:");
            System.out.println("[1] Adivinar una letra");
            System.out.println("[2] Adivinar el título completo");
            System.out.println("[3] Salir del juego");
            System.out.print("Selecciona una opción: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (choice) {
                case 1 -> guessLetter(scanner);
                case 2 -> guessTitle(scanner);
                case 3 -> {
                    System.out.println("Has salido del juego.");
                    return;
                }
                default -> System.out.println("Opción no válida. Intenta nuevamente.");
            }

            if (isGameWon()) {
                System.out.println("¡Felicidades! Has adivinado la película: " + selectedMovie.getTitle());
                player.addScore(20);
                break;
            }
        }

        if (remainingAttempts == 0) {
            System.out.println("Te has quedado sin intentos. La película era: " + selectedMovie.getTitle());
        }

        System.out.println("Puntuación final: " + player.getScore());

        // Al finalizar el juego, intentamos guardar el ranking si la puntuación es relevante
        saveRanking();
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

    private void saveRanking() {
        List<Player> ranking = Player.loadRankingFromFile();

        if (ranking.size() < 5 || player.getScore() > ranking.get(4).getScore()) {
            // Si el jugador entra en el ranking de las 5 mejores puntuaciones
            boolean nicknameExists = true; // Empezamos asumiendo que el nickname no existe.
            while (nicknameExists) {
                System.out.print("Introduce tu nickname para el ranking: ");
                String nickname = new Scanner(System.in).nextLine();

                // Verificar si el nickname ya existe
                boolean exists = false;
                for (Player p : ranking) {
                    if (p.getNickname().equalsIgnoreCase(nickname)) {
                        System.out.println("El nickname ya existe. Intenta con otro.");
                        exists = true;
                        break; // Si el nickname ya existe, salimos del bucle
                    }
                }

                if (!exists) {
                    // Si el nickname no existe, lo asignamos al jugador y salimos del bucle
                    ranking.add(new Player(nickname, player.getScore()));
                    // Ordenar el ranking en orden descendente de puntuaciones
                    ranking.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
                    // Limitar el ranking a las 5 mejores puntuaciones
                    if (ranking.size() > 5) {
                        ranking.remove(5);
                    }
                    // Guardar el ranking actualizado en el archivo
                    Player.saveRankingToFile(ranking);
                    nicknameExists = false; // Salir del bucle
                }
            }
        } else {
            System.out.println("Tu puntuación no entra en el ranking de las mejores 5.");
        }


        // Mostrar el ranking
        System.out.println("\nRanking de puntuaciones:");
        for (Player p : ranking) {
            System.out.println(p.getNickname() + " - " + p.getScore());
        }
    }

    public static void main(String[] args) {
        String filePath = "data/movies.txt"; // Ruta actualizada para el archivo de películas

        try {
            List<Movie> movies = Movie.loadMoviesFromFile(filePath);
            Game game = new Game(movies);
            game.start();
        } catch (IOException e) {
            System.err.println("Error al cargar las películas: " + e.getMessage());
        }
    }
}
