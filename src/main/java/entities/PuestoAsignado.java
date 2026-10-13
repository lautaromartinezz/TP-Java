package entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PuestoAsignado {
	private int id;
	private Monster monster;
	private Puesto puesto;
	private Date fecha;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Monster getMonster() {
		return monster;
	}
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	public Puesto getPuesto() {
		return puesto;
	}
	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getFechaFormateada() {
		if (fecha == null) { return ""; }
		return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
	}

	public String getFechaInput() {
		if (fecha == null) { return ""; }
		return new SimpleDateFormat("yyyy-MM-dd").format(fecha);
	}

	@Override
	public String toString() {
		return "\nPuestoAsignado [id=" + id + ", monster=" + monster + ", puesto=" + puesto + ", fecha=" + fecha + "]";
	}
}
