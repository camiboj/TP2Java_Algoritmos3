package cartas;

public abstract class Invocacion {
    Carta carta;

    public Invocacion(Carta unaCarta) {
        carta = unaCarta;
    }

    public Carta invocar () { return carta; }
}
