package tateti;

 
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tateti extends Application {

    private final char[][] tablero;
    private char jugadorActual;
    private Button[][] botones;
    private int puntajeX;
    private int puntajeO;
    private int partidosJugados;
    private Text resultadoParcial;

    public Tateti() {
        tablero = new char[3][3];
        jugadorActual = 'X'; // Comienza el jugador X
        inicializarTablero();
        puntajeX = 0;
        puntajeO = 0;
        partidosJugados = 0;
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '-';
            }
        }
    }

    private boolean hayGanador() {
        // Comprobamos filas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == jugadorActual && tablero[i][1] == jugadorActual && tablero[i][2] == jugadorActual) {
                return true;
            }
        }

        // Comprobamos columnas
        for (int j = 0; j < 3; j++) {
            if (tablero[0][j] == jugadorActual && tablero[1][j] == jugadorActual && tablero[2][j] == jugadorActual) {
                return true;
            }
        }

        // Comprobamos diagonales
        if (tablero[0][0] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][2] == jugadorActual) {
            return true;
        }

        if (tablero[0][2] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][0] == jugadorActual) {
            return true;
        }

        return false;
    }

    private boolean tableroCompleto() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    private void actualizarTablero(Posicion posicion) {
        int fila = posicion.fila;
        int columna = posicion.columna;
        Button boton = posicion.boton;

        tablero[fila][columna] = jugadorActual;
        boton.setText(String.valueOf(jugadorActual));
        boton.setDisable(true);

        if (hayGanador()) {
            mostrarMensaje("¡El jugador " + jugadorActual + " ha ganado el partido!");

            if (jugadorActual == 'X') {
                puntajeX++;
            } else {
                puntajeO++;
            }

            partidosJugados++;
            reiniciarPartido();
        } else if (tableroCompleto()) {
            mostrarMensaje("¡Es un empate!");
            partidosJugados++;
            reiniciarPartido();
        } else {
            jugadorActual = (jugadorActual == 'X') ? 'O' : 'X'; // Cambiar al siguiente jugador
        }

        if (partidosJugados == 5) {
            mostrarGanadorDelJuego();
            reiniciarPartido();
        }

        actualizarResultadoParcial();
    }

    private void mostrarGanadorDelJuego() {
        String ganador;

        if (puntajeX > puntajeO) {
            ganador = "Jugador X";
        } else if (puntajeO > puntajeX) {
            ganador = "Jugador O";
        } else {
            ganador = "Empate";
        }

        mostrarMensaje("El ganador es: " + ganador);
    }

   private void mostrarMensaje(String mensaje) {
    Stage ventanaMensaje = new Stage();
    StackPane contenedor = new StackPane();
    Text texto = new Text(mensaje);
    texto.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    contenedor.getChildren().add(texto);
    contenedor.setAlignment(Pos.CENTER);
    
    Button botonCerrar = new Button("Cerrar");
    botonCerrar.setOnAction(e -> ventanaMensaje.close());
    
    VBox contenedorPrincipal = new VBox(20);
    contenedorPrincipal.setAlignment(Pos.CENTER);
    contenedorPrincipal.getChildren().addAll(contenedor, botonCerrar);
    
    Scene escena = new Scene(contenedorPrincipal, 200, 100);
    ventanaMensaje.setScene(escena);
    ventanaMensaje.setTitle("Resultado del partido");
    ventanaMensaje.setMinWidth(150);
    ventanaMensaje.setMinHeight(100);
    ventanaMensaje.show();
}


    private void reiniciarPartido() {
        inicializarTablero();

        // Habilitar nuevamente los botones y limpiar sus textos
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                Button boton = botones[fila][columna];
                boton.setDisable(false);
                boton.setText("");
            }
        }

        actualizarTableroGUI();
    }

    private void actualizarResultadoParcial() {
        resultadoParcial.setText("Gano          x = " + puntajeX + "\nGano          o = " + puntajeO + "\nEmpatados   = " + (partidosJugados - puntajeX - puntajeO));
    }

    private void actualizarTableroGUI() {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                Button boton = botones[fila][columna];
                boton.setText(String.valueOf(tablero[fila][columna]));
                boton.setDisable(tablero[fila][columna] != '-');
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane tableroGrid = new GridPane();
        tableroGrid.setAlignment(Pos.CENTER);
        tableroGrid.setHgap(10);
        tableroGrid.setVgap(10);

        botones = new Button[3][3];

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                Button boton = new Button();
                boton.setMinSize(60, 60);
                boton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #800080; -fx-font-size: 18px;");
                Posicion posicion = new Posicion(fila, columna, boton);
                boton.setOnAction(e -> {
                    actualizarTablero(posicion);
                });
                botones[fila][columna] = boton;
                tableroGrid.add(boton, columna, fila);
            }
        }

        resultadoParcial = new Text("Gano          x = 0\nGano          o = 0\nEmpatados   = 0");
        resultadoParcial.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox contenedorPrincipal = new VBox(20);
        contenedorPrincipal.setAlignment(Pos.CENTER);
        contenedorPrincipal.getChildren().addAll(tableroGrid, resultadoParcial);

        Scene scene = new Scene(contenedorPrincipal, 300, 400);
        primaryStage.setTitle("Ta-Te-Ti");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class Posicion {
        private final int fila;
        private final int columna;
        private final Button boton;

        public Posicion(int fila, int columna, Button boton) {
            this.fila = fila;
            this.columna = columna;
            this.boton = boton;
        }
    }
}
