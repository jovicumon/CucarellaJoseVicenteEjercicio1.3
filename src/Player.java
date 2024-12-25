import java.io.Serializable;

/**
 * Clase que representa al jugador.
 */
public class Player implements Serializable {
    private String nickname;
    private int score;

    public Player(String nickname) {
        this.nickname = nickname;
        this.score = 0;
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
}
