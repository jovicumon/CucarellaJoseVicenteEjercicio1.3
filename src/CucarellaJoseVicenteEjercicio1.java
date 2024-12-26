/**
 * Clase principal que gestiona el juego.
 *
 * <p>Esta clase inicializa y ejecuta el juego de adivinar películas
 * utilizando la clase {@link Game}. Maneja cualquier excepción que
 * pueda ocurrir durante la carga o ejecución del juego.</p>
 */
public class CucarellaJoseVicenteEjercicio1 {

    /**
     * Método principal que arranca la aplicación.
     *
     * @param ignoredArgs Argumentos de línea de comandos (no utilizados).
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
