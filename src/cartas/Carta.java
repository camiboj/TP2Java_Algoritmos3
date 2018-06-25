package cartas;
import estadosDeCartas.BocaAbajo;
import estadosDeCartas.BocaArriba;
import estadosDeCartas.Estado;
import tablero.InterrumpirAtaqueException;

public abstract class Carta {
    protected String nombre;
    private Estado estado;
    
    
    public Carta(String unNombre) {
        this.nombre = unNombre;
        this.estado = null;
    }

    public void colocarBocaArriba() throws InterrumpirAtaqueException {
        this.estado = new BocaArriba();
        this.activarEfecto();
    }

    public abstract void activarEfecto();

    public void colocarBocaAbajo() {
    	this.estado = new BocaAbajo();
	}
	public Estado getEstado() {
		return estado;
	}

    public boolean equals(Object object){return this.getClass().equals(object.getClass());}
}
