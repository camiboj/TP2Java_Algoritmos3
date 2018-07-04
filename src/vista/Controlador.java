package vista;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.jugador.Jugador;
import modelo.jugador.YuGiOh;
import modelo.tablero.Tablero;
import vista.botones.BotonCartaBocaAbajo;
import vista.botones.Botonera;
import vista.handler.MazoHandler;

import java.util.ArrayList;

public class Controlador {
    private Stage stage;
    private Botonera botonera;
    private Jugador jugadorContrincante;
    private Jugador jugadorTurno;
    private ArrayList<Jugador> jugadores;
    private YuGiOh juego;
    private ContenedorBase contenedorBase;
    private VistaJugador vistaActual;
    private VistaJugador vistaContrincante;

    public Controlador(Stage stage, YuGiOh juego, Tablero tablero){
        ContenedorBase contenedorBase = new ContenedorBase(stage, juego, tablero);
        this.jugadorTurno = juego.obtenerJugador1();
        this.jugadorContrincante = juego.obtenerJugador2();
        this.vistaActual = new VistaJugador(contenedorBase, jugadorTurno,
                tablero);
        this.vistaContrincante = new VistaJugador(contenedorBase, jugadorContrincante,
                tablero);
        this.contenedorBase = contenedorBase;
        this.botonera = new Botonera(this);
        this.jugadores = juego.obtenerJugadores();
        this.juego = juego;
        this.stage = stage;
        contenedorBase.escribirEnConsola("Es el turno de " + jugadorTurno.obtenerNombre() + "\n Inicio Fase Inicial: Haz click en el Mazo para obtener una carta");
        this.botonera();
        vistaActual.activar(true);
        vistaContrincante.activar(false);
        this.setMazo();
    }

    private void setMazo() {
        BotonCartaBocaAbajo boton = new BotonCartaBocaAbajo(3,8);
        contenedorBase.ubicarObjeto(boton, 3, 8);
        boton.setOnAction(new MazoHandler(juego, this.vistaActual.getVistaMano(), jugadorTurno, boton,
                botonera.obtenerBotonPreparacion()));
    }

    public void botonera() {
        contenedorBase.setBotonera(botonera);
    }

    public void cambiarTurno() {
        vistaActual.reset();
        vistaContrincante.reset();

        Jugador jugadorAux = jugadorTurno;
        jugadorTurno = jugadorContrincante;
        jugadorContrincante = jugadorAux;

        VistaJugador vistaAux = vistaActual;
        vistaActual = vistaContrincante;
        vistaContrincante = vistaAux;

        contenedorBase.escribirEnConsola("Es el turno de " + jugadorTurno.obtenerNombre());
        vistaActual.activar(true);
        vistaContrincante.activar(false);
    }

    public void mostrar(Stage stage) {
        Scene escenaJuego = new Scene(contenedorBase, 640, 480);
        stage.setScene(escenaJuego);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
    }
}
