package entities;

public class Puerta {
	private int id;
	private String numero;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "\nPuerta [id=" + id + ", numero=" + numero + "]";
	}
}
