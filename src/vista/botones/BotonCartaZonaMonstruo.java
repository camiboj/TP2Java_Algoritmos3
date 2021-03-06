package vista.botones;
import modelo.cartas.cartasMonstruo.CartaMonstruo;

public class BotonCartaZonaMonstruo extends BotonCarta {
    private CartaMonstruo carta;

    public BotonCartaZonaMonstruo(CartaMonstruo carta, int fila, int columna){
        super(carta, fila, columna);
        this.carta = carta;
        this.settearTooltip("Efecto: " + carta.obtenerEfecto() +
                "\n ATK: " + String.valueOf(carta.obtenerPuntosAtaque().obtenerNumero()) +
                "\n DEF: " + String.valueOf(carta.obtenerPuntosDefensa().obtenerNumero()));
    }

    @Override
    public CartaMonstruo obtenerCarta() {
        return carta;
    }

    public void actualizarDatos() {
        this.settearTooltip("Efecto: " + carta.obtenerEfecto() +
                "\n ATK: " + String.valueOf(carta.obtenerPuntosAtaque().obtenerNumero()) +
                "\n DEF: " + String.valueOf(carta.obtenerPuntosDefensa().obtenerNumero()));
    }
}
