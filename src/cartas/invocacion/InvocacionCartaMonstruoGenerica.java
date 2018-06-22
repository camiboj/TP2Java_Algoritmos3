package cartas.invocacion;

import cartas.cartasMonstruo.CartaMonstruo;

import java.util.ArrayList;
import java.util.List;

public class InvocacionCartaMonstruoGenerica extends InvocacionCartaMonstruo {

    public InvocacionCartaMonstruoGenerica(CartaMonstruo unaCarta, List<CartaMonstruo> sacrificios) {
        super(unaCarta, sacrificios);
        carta = unaCarta;
        this.sacrificios = sacrificios;
    }

    public InvocacionCartaMonstruoGenerica(CartaMonstruo unaCarta) {
        super(unaCarta, new ArrayList<>());
        carta = unaCarta;
    }
    //Le podemos pasar el cementerio y la zona monstruo para que esta opere
    public List<CartaMonstruo> mostrarCartasASacrificar() {
        return sacrificios;
    }

    public boolean debeSacrificar() {
        if (sacrificios== null) return false;
        return sacrificios.size()>0;
    }
}