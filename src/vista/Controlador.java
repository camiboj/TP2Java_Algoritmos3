package vista;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Fase.FasePreparacion;
import modelo.cartas.cartasMonstruo.CartaMonstruo;
import modelo.cartas.cartasMonstruo.Jinzo7;
import modelo.excepciones.CartaAtacanteInexistenteException;
import modelo.excepciones.CartaDefensoraInexistenteException;
import modelo.excepciones.NoHayTrampasExcepcion;
import modelo.excepciones.VictoriaException;
import modelo.jugador.Jugador;
import modelo.jugador.YuGiOh;
import modelo.tablero.Tablero;
import vista.botones.*;
import vista.handler.BotonAtacarHandler;
import vista.handler.BotonFinFaseAtaqueHandler;
import vista.handler.MazoHandler;
import vista.handler.OpcionesAtacarHandler;
import vista.vistaZonas.VistaMano;

import java.util.ArrayList;
import java.util.List;

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
    private FasePreparacion fasePreparacion;
    private ArrayList<CheckBoxCarta> checks;
    private List<BotonCarta> botonesTrampaMagicaActivados;
    private MediaPlayer mediaplayer;

    public Controlador(Stage stage, YuGiOh juego, Tablero tablero) {
        ContenedorBase contenedorBase = new ContenedorBase(stage, juego, tablero);
        this.botonesTrampaMagicaActivados = new ArrayList <>();
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
        this.settearSonido("sonidos/youAreWinning.mp3");
        this.fasePreparacion = new FasePreparacion();
        vistaActual.activar(true, fasePreparacion, this);
        vistaContrincante.activar(false, fasePreparacion, this);
        this.setMazo();
    }

    private void settearSonido(String cancion) {
        String path = Main.class.getResource(cancion).toString();

        Media file = new Media(path);
        this.mediaplayer = new MediaPlayer(file);
        mediaplayer.setAutoPlay(true);
        mediaplayer.setVolume(0.3);
        mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaplayer.play();
    }

    public void setMazo() {
        BotonCartaBocaAbajo boton = new BotonCartaBocaAbajo(3, 8);
        contenedorBase.ubicarObjeto(boton, 3, 8);
        boton.setOnAction(new MazoHandler(juego, this.vistaActual.getVistaMano(), jugadorTurno, boton,
                botonera.obtenerBotonPreparacion(), fasePreparacion, contenedorBase, this));
    }

    public void botonera() {
        contenedorBase.setBotonera(botonera);
    }

    public void cambiarTurno() {

        eliminarMagicasActivadas();
        botonera.desactivarCambiarTurno();
        vistaActual.resetNombre();
        vistaContrincante.resetNombre();
        this.fasePreparacion = new FasePreparacion();

        Jugador jugadorAux = jugadorTurno;
        jugadorTurno = jugadorContrincante;
        jugadorContrincante = jugadorAux;

        VistaJugador vistaAux = vistaActual;
        vistaActual = vistaContrincante;
        vistaContrincante = vistaAux;

        mediaplayer.stop();
        if (jugadorTurno.obtenerPuntos().obtenerNumero() >= jugadorContrincante.obtenerPuntos().obtenerNumero()) {
            this.settearSonido("sonidos/youAreWinning.mp3");
        }
        else {
            this.settearSonido("sonidos/youAreLosing.mp3");
        }

        contenedorBase.escribirEnConsola("Es el turno de " + jugadorTurno.obtenerNombre() + "\n" +
                "Inicio Fase Inicial: Haz click en el Mazo para obtener una carta"
        );
        this.setMazo();
        vistaActual.activar(true, fasePreparacion, this);
        vistaContrincante.activar(false, fasePreparacion, this);
    }

    public void mostrar(Stage stage) {
        Scene escenaJuego = new Scene(contenedorBase, 640, 480);
        stage.setScene(escenaJuego);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
    }

    public ContenedorBase obtenerContenedorBase() {
        return contenedorBase;
    }

    public VistaMano obtenerVistaMano() {
        return vistaActual.getVistaMano();
    }

    public VistaJugador obtenerVistaJugador() {
        return vistaActual;
    }

    public void setOpcionAtacar() {

        ContextMenuAtacante contextMenu = new ContextMenuAtacante();
        MenuItem opcionAtacar = new MenuItem("Atacar");

        opcionAtacar.setOnAction(new OpcionesAtacarHandler(this, contextMenu));

        contextMenu.getItems().addAll(opcionAtacar);
        vistaActual.setOpcionAtacar(contextMenu);
    }

    public void generarOpcionesAtaque(ContextMenuAtacante contextMenuAtacante) {
        List<CheckBoxCarta> opciones = vistaContrincante.generarOpcionesAtaque();
        this.checks = new ArrayList<>();
        int columna = 3;
        for (CheckBoxCarta carta : opciones) {
            if (carta != null) {
                this.checks.add(carta);
            }
            contenedorBase.ubicarObjeto(carta, 1, columna);
            columna++;
        }
        botonera.activarBotonAtacar(new BotonAtacarHandler(checks, contenedorBase, this, contextMenuAtacante));
    }

    public void atacarMonstruos(BotonCartaZonaMonstruo botonMonstruoAtacante, BotonCarta botonCartaAtacada) throws CartaAtacanteInexistenteException, CartaDefensoraInexistenteException {

        Tablero tablero = juego.mostrarTablero();
        CartaMonstruo cartaAtacante = (CartaMonstruo) botonMonstruoAtacante.obtenerCarta();
        try {
            cartaAtacante.colocarBocaArriba();
        } catch (VictoriaException e) {
        }

        CartaMonstruo cartaDefensora = (CartaMonstruo) botonCartaAtacada.obtenerCarta();

        List<CartaMonstruo> cartasMuertas = tablero.atacarDosMonstruos(cartaAtacante, jugadorTurno,
                cartaDefensora, jugadorContrincante);


        for (CartaMonstruo cartaMuerta : cartasMuertas) {
            if (vistaActual.obtenerBoton(cartaMuerta) != null) {
                BotonCarta botonCarta = vistaActual.obtenerBoton(cartaMuerta);
                vistaActual.eliminarElemento(botonCarta);
                contenedorBase.getChildren().remove(botonCarta);
                continue;
            }

            BotonCarta botonCarta = vistaContrincante.obtenerBoton(cartaMuerta);
            vistaContrincante.eliminarElemento(botonCarta);
            contenedorBase.getChildren().remove(botonCarta);
        }
        this.actualizarZonaMonstruo();
    }

    public void iniciarFaseTrampa() {
        botonera.activarFinDeTrampas(new BotonFinFaseAtaqueHandler(contenedorBase, this));
        contenedorBase.escribirEnConsola("Has podido atacar correctamente. Se activó la fase trampa " +
                "automáticamente como resultado de la misma. \n" +
                " Para pasar a la fase final haz click en 'Fin Fase de Ataque' y para volver a atacar haz click " +
                "en 'Atacar'");
        try {
            BotonCarta trampaActivada = vistaContrincante.voltearPrimeraTrampa(this);
            vistaActual.actualizarDatosCartas();
            vistaContrincante.actualizarDatosCartas();
            botonesTrampaMagicaActivados.add(trampaActivada);
        } catch (NoHayTrampasExcepcion noHayTrampasExcepcion) {
            contenedorBase.escribirEnConsola("Has podido atacar correctamente y no se activaron trampas. " +
                    "Para pasar a la fase final haz click en 'Fin Fase de Ataque' y para volver a atacar haz click " +
                    "en 'Atacar'");
        }

    }

    public void activarFaseFinal() {
        vistaActual.activarCartasMagicas(this);
    }

    public void eliminarMagicasActivadas() {
        for (BotonCarta botonCarta : botonesTrampaMagicaActivados) {

            vistaContrincante.eliminarElemento(botonCarta);
            vistaActual.eliminarElemento(botonCarta);
            contenedorBase.getChildren().remove(botonCarta);

        }
        botonesTrampaMagicaActivados = new ArrayList<>();
    }

    public void agregarCartaTrampaMagicaABorrar(BotonCarta botonCarta) {
        botonesTrampaMagicaActivados.add(botonCarta);
    }


    public void activarFinTurno() {
        botonera.activarFinDeTurno();
    }

    public void actualizarZonaMonstruo() {
        Tablero tablero = juego.mostrarTablero();
        List<CartaMonstruo> monstruosPropios = tablero.mostrarZonaMonstruo(jugadorTurno).obtenerMonstruos();
        List<CartaMonstruo> monstruosAjenos = tablero.mostrarZonaMonstruo(jugadorContrincante).obtenerMonstruos();

        vistaActual.actualizarMonstruos(monstruosPropios);
        vistaContrincante.actualizarMonstruos(monstruosAjenos);
    }

    public void actualizarDatosZonaMonstruo() {
        vistaContrincante.actualizarDatosCartas();
        vistaActual.actualizarDatosCartas();

    }

    public void actualizarMano() {
        vistaActual.actualizarMano(this);
    }
}
