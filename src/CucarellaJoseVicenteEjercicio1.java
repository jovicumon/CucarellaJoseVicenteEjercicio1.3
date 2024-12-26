public class CucarellaJoseVicenteEjercicio1 {
    /**
     * Clase principal que gestiona el juego.
     */

    public static void main(String[] ignoredArgs) {
        try {
            Game game = new Game();
            game.start();
        } catch (Exception e) {
            System.err.println("Error al cargar el juego: " + e.getMessage());
        }
    }

}
