import java.io.*;
import java.util.*;

public class Player implements Serializable {
    private String nickname;
    private int score;

    public Player(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void subtractScore(int points) {
        this.score -= points;
    }

    // Guardar el ranking a un archivo binario
    public static void saveRankingToFile(List<Player> ranking) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/ranking.dat"))) {
            out.writeObject(ranking); // Escribir el ranking completo al archivo
        } catch (IOException e) {
            System.err.println("Error al guardar el ranking: " + e.getMessage());
        }
    }

    // Cargar el ranking desde el archivo binario
    public static List<Player> loadRankingFromFile() {
        List<Player> ranking = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("data/ranking.dat"))) {
            ranking = (List<Player>) in.readObject(); // Leer el ranking desde el archivo
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, creamos un ranking vacío
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el ranking: " + e.getMessage());
        }
        return ranking;
    }
}