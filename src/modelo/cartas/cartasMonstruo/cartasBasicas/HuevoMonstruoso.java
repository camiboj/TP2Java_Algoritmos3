package modelo.cartas.cartasMonstruo.cartasBasicas;;
import modelo.tablero.Cementerio;
import modelo.cartas.cartasMonstruo.CartaMonstruo;

public class HuevoMonstruoso extends CartaMonstruo {


	public HuevoMonstruoso() {

		super("Huevo Monstruoso", 600, 900, 3);
	}

	public Object getUbicacion() {
        return new Cementerio();
    }
}
