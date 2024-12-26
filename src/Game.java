import java.io.*;
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
    private List<Player> ranking;
    static final String rutaMovies = "data/movies.txt";
    static final String rutaRanking = "data/ranking.dat";

    public Game(List<Movie> movies) {
        this.movies = movies;
        this.guessedLetters = new HashSet<>();
        this.ranking = loadRankingFromFile();  // Cargar el ranking desde el archivo binario
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Selección de película aleatoria
        Random random = new Random();
        selectedMovie = movies.get(random.nextInt(movies.size()));

        System.out.println("🎯 🎯 🎯 Adivina la película 🎯 🎯 🎯");
        System.out.println("El título de la película tiene " + selectedMovie.getTitle().length() + " caracteres (incluidos espacios y signos de puntuación)");
        System.out.println("La película a adivinar es: " + selectedMovie.getHiddenTitle());

        player = new Player();

        remainingAttempts = 10; // Asignamos el valor de intentos

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

            // Solo se ejecuta si la opción fue 1 o 2
            if (choice == 1 || choice == 2) {
                if (isGameWon()) {
                    System.out.println("¡Felicidades! Has adivinado la película: " + selectedMovie.getTitle());
                    player.addScore(20);
                    break;
                }
            }
        }

        if (remainingAttempts == 0) {
            System.out.println("Te has quedado sin intentos. La película era: " + selectedMovie.getTitle());
        }

        System.out.println("Puntuación final: " + player.getScore());

        // Verifica si el jugador entra en el ranking
        // Verifica si el jugador entra en el ranking
        if (ranking.size() < 5 || player.getScore() > ranking.get(4).getScore()) {
            String nicknameInput = null;
            boolean nicknameValid = false;

            // Bucle para obtener un nickname válido
            while (!nicknameValid) {
                System.out.print("Introduce tu nickname para el ranking: ");
                nicknameInput = scanner.nextLine().trim();

                // Comprobar si el nickname ya existe en los primeros 4 lugares
                boolean nicknameExists = false;
                int limit = Math.min(4, ranking.size());  // Comprobar solo los primeros 4 lugares
                for (int i = 0; i < limit; i++) {
                    if (ranking.get(i).getNickname().equalsIgnoreCase(nicknameInput)) {
                        nicknameExists = true;
                        break;  // Si encontramos el nickname, ya no necesitamos seguir buscando
                    }
                }

                if (nicknameExists) {
                    System.out.println("El nickname ya existe. Intenta con otro.");
                } else {
                    nicknameValid = true;  // Si el nickname es válido, salimos del bucle
                }
            }
            Player newPlayer = new Player();
            newPlayer.setNickname(nicknameInput);
            newPlayer.addScore(player.getScore());
            ranking.add(newPlayer);
            ranking.sort((p1, p2) -> p2.getScore() - p1.getScore()); // Ordenar el ranking

            // Mantener solo las 5 mejores puntuaciones
            if (ranking.size() > 5) {
                ranking.remove(5);
            }

            saveRankingToFile(ranking);  // Guardar el ranking actualizado en el archivo

        } else {
            System.out.println("Tu puntuación no entra en el ranking.");
        }

        // Mostrar el ranking final
        System.out.println("\nRanking de puntuaciones:");
        for (Player p : ranking) {
            System.out.println(p.getNickname() + ": " + p.getScore());
        }
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

    // Cargar el ranking desde el archivo binario
    public static List<Player> loadRankingFromFile() {
        List<Player> ranking = new ArrayList<>();
        try {
            FileInputStream fi = new FileInputStream(rutaRanking);
            //Comprobación ranking.dat está vacío
            if (fi.available() > 0) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(rutaRanking));
                Object obj = in.readObject();
                if (obj instanceof List<?> list && !list.isEmpty() && list.getFirst() instanceof Player) {
                    ranking = (List<Player>) list;
                }
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, creamos un ranking vacío
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el ranking: " + e.getMessage());
        }
        return ranking;
    }

    // Guardar el ranking actualizado en el archivo binario
    public static void saveRankingToFile(List<Player> ranking) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rutaRanking))) {
            out.writeObject(ranking);
        } catch (IOException e) {
            System.err.println("Error al guardar el ranking: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            List<Movie> movies = Movie.loadMoviesFromFile(rutaMovies);
            Game game = new Game(movies);
            game.start();
        } catch (IOException e) {
            System.err.println("Error al cargar las películas: " + e.getMessage());
        }
    }
}
