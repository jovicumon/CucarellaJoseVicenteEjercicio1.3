import java.io.Serializable;

/**
 * Clase que representa al jugador.
 */
public class Player implements Serializable {
    private String nickname;
    private int score;

    /**
     * Constructor que inicializa un jugador con una puntuación inicial de 0.
     */
    public Player() {
        this.score = 0;
    }

    /**
     * Establece el apodo del jugador.
     *
     * @param nickname El apodo que se asignará al jugador.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Obtiene el apodo del jugador.
     *
     * @return El apodo del jugador.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Obtiene la puntuación actual del jugador.
     *
     * @return La puntuación actual del jugador.
     */
    public int getScore() {
        return score;
    }

    /**
     * Incrementa la puntuación del jugador en una cantidad específica de puntos.
     *
     * @param points La cantidad de puntos a añadir a la puntuación del jugador.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Reduce la puntuación del jugador en una cantidad específica de puntos.
     *
     * @param points La cantidad de puntos a restar de la puntuación del jugador.
     */
    public void subtractScore(int points) {
        this.score -= points;
    }
}
