import modelo.Fase.FasePreparacion;
import modelo.cartas.cartasCampo.Wasteland;
import modelo.cartas.cartasMonstruo.cartasBasicas.AlasDeLaLlamaPerversa;
import modelo.cartas.cartasMonstruo.cartasBasicas.HuevoMonstruoso;
import modelo.cartas.invocacion.InvocacionCartaMonstruoGenerica;
import modelo.cartas.invocacion.InvocacionDefault;
import modelo.excepciones.InvocacionExcepcion;
import modelo.excepciones.VictoriaException;
import modelo.excepciones.ZonaMonstruoLlenaException;
import modelo.jugador.Jugador;
import org.junit.Test;
import modelo.tablero.Tablero;
import modelo.tablero.ZonaMonstruo;

import static junit.framework.TestCase.assertTrue;

public class WastelandTest {

    @Test
    public void activacionCartaWastelandHaceLoEsperado () throws VictoriaException {
        Jugador jugador1 = new Jugador();
        Jugador jugador2 = new Jugador();
        Tablero tablero = new Tablero(jugador1, jugador2);

        HuevoMonstruoso monstruo1 = new HuevoMonstruoso();
        FasePreparacion fasePreparacion1 = new FasePreparacion();
        InvocacionCartaMonstruoGenerica invocacionMonstruo1 = new InvocacionCartaMonstruoGenerica(monstruo1,
                fasePreparacion1);

        AlasDeLaLlamaPerversa monstruo2 = new AlasDeLaLlamaPerversa();
        FasePreparacion fasePreparacion2 = new FasePreparacion();
        InvocacionCartaMonstruoGenerica invocacionMonstruo2 = new InvocacionCartaMonstruoGenerica(monstruo2,
                fasePreparacion2);
        try {
            tablero.colocarZonaMonstruo(invocacionMonstruo1, jugador1);
            tablero.colocarZonaMonstruo(invocacionMonstruo2, jugador2);
        } catch (ZonaMonstruoLlenaException e) {
        } catch (InvocacionExcepcion invocacionExcepcion) {
        }

        ZonaMonstruo zonaMonstruo1 = tablero.mostrarZonaMonstruo(jugador1);
        ZonaMonstruo zonaMonstruo2 = tablero.mostrarZonaMonstruo(jugador2);
        //Verifico que los dos monstruos están en el campo
        assertTrue(zonaMonstruo1.existe(monstruo1) && zonaMonstruo2.existe(monstruo2));

        Wasteland wasteland = new Wasteland();
        InvocacionDefault invocacionWasteland = new InvocacionDefault(wasteland);
        tablero.colocarZonaCampo(invocacionWasteland, jugador1);

        assertTrue(monstruo1.obtenerPuntosAtaque().obtenerNumero() == 800);
        assertTrue(monstruo1.obtenerPuntosDefensa().obtenerNumero() == 900);
        assertTrue(monstruo2.obtenerPuntosAtaque().obtenerNumero() == 700);
        assertTrue(monstruo2.obtenerPuntosDefensa().obtenerNumero() == 900);
    }
}
