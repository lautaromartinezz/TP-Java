package entities;

public class PuertaAsignada {
	private int id;
	private PuestoAsignado puestoAsignado;
	private Puerta puerta;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PuestoAsignado getPuestoAsignado() {
		return puestoAsignado;
	}
	public void setPuestoAsignado(PuestoAsignado puestoAsignado) {
		this.puestoAsignado = puestoAsignado;
	}
	public Puerta getPuerta() {
		return puerta;
	}
	public void setPuerta(Puerta puerta) {
		this.puerta = puerta;
	}

	@Override
	public String toString() {
		return "\nPuertaAsignada [id=" + id + ", puestoAsignado=" + puestoAsignado + ", puerta=" + puerta + "]";
	}
}
