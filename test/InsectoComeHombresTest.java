import modelo.Fase.FasePreparacion;
import modelo.cartas.cartasMonstruo.InsectoComeHombres;
import modelo.cartas.cartasMonstruo.cartasBasicas.AlasDeLaLlamaPerversa;
import modelo.cartas.invocacion.InvocacionCartaMonstruoGenerica;
import modelo.excepciones.InvocacionExcepcion;
import modelo.excepciones.VictoriaException;
import modelo.excepciones.ZonaMonstruoLlenaException;
import modelo.jugador.Jugador;
import org.junit.Test;
import modelo.tablero.Cementerio;
import modelo.tablero.Tablero;
import modelo.tablero.ZonaMonstruo;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class InsectoComeHombresTest {

    @Test
    public void InsectoComeHombreMataCartaAtacante () throws VictoriaException {

        Jugador jugadorDefensor = new Jugador();
        Jugador jugadorAtacante = new Jugador();
        Tablero tablero = new Tablero(jugadorAtacante, jugadorDefensor);

        InsectoComeHombres insectoComeHombres = new InsectoComeHombres();
        insectoComeHombres.colocarEnModoDeDefensa();
        FasePreparacion fasePreparacionInsecto = new FasePreparacion();
        InvocacionCartaMonstruoGenerica invocacionInsectoComeHombres = new InvocacionCartaMonstruoGenerica(insectoComeHombres,
                fasePreparacionInsecto);
        try {
            tablero.colocarZonaMonstruo(invocacionInsectoComeHombres, jugadorDefensor);
        } catch (ZonaMonstruoLlenaException e) {
        } catch (InvocacionExcepcion invocacionExcepcion) {
        }

        AlasDeLaLlamaPerversa cartaAtacante = new AlasDeLaLlamaPerversa();
        FasePreparacion fasePreparacionAtacante = new FasePreparacion();
        InvocacionCartaMonstruoGenerica invocacionCartaAtacante = new InvocacionCartaMonstruoGenerica(cartaAtacante,
                fasePreparacionAtacante);
        try {
            tablero.colocarZonaMonstruo(invocacionCartaAtacante, jugadorAtacante);
        } catch (ZonaMonstruoLlenaException e) {
        } catch (InvocacionExcepcion invocacionExcepcion) {
        }

        ZonaMonstruo zonaMonstruoAtacante = tablero.mostrarZonaMonstruo(jugadorAtacante);

        tablero.atacarDosMonstruos(cartaAtacante, jugadorAtacante, insectoComeHombres, jugadorDefensor);
        assertFalse(zonaMonstruoAtacante.existe(cartaAtacante));

        Cementerio cementerioAtacante = tablero.mostrarCementerio(jugadorAtacante);
        assertTrue(cementerioAtacante.existe(cartaAtacante));

        ZonaMonstruo zonaMonstruoDefensor = tablero.mostrarZonaMonstruo(jugadorDefensor);
        assertTrue(zonaMonstruoDefensor.existe(insectoComeHombres));

        Cementerio cementerioDefensor = tablero.mostrarCementerio(jugadorDefensor);
        assertFalse(cementerioDefensor.existe(insectoComeHombres));

        assertTrue(jugadorDefensor.obtenerPuntos().obtenerNumero() == 8000);
        assertTrue(jugadorAtacante.obtenerPuntos().obtenerNumero() == 8000);
    }
}

