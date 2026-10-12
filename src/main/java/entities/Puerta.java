package entities;

public class Puerta {
	private int id;
	private String numero;
	private Puesto puesto;

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
	public Puesto getPuesto() {
		return puesto;
	}
	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	@Override
	public String toString() {
		return "\nPuerta [id=" + id + ", numero=" + numero + ", puesto=" + puesto + "]";
	}
}
