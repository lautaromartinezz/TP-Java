package entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Resultado {
	private int id;
	private Date fecha;
	private int energiaGenerada;
	private String nombreNino;
	private Monster monster;
	private Puerta puerta;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getEnergiaGenerada() {
		return energiaGenerada;
	}
	public void setEnergiaGenerada(int energiaGenerada) {
		this.energiaGenerada = energiaGenerada;
	}
	public String getNombreNino() {
		return nombreNino;
	}
	public void setNombreNino(String nombreNino) {
		this.nombreNino = nombreNino;
	}
	public Monster getMonster() {
		return monster;
	}
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	public Puerta getPuerta() {
		return puerta;
	}
	public void setPuerta(Puerta puerta) {
		this.puerta = puerta;
	}

	public String getFechaFormateada() {
		if (fecha == null) { return ""; }
		return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
	}

	public String getFechaInput() {
		if (fecha == null) { return ""; }
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(fecha);
	}

	@Override
	public String toString() {
		return "\nResultado [id=" + id + ", fecha=" + fecha + ", energiaGenerada=" + energiaGenerada
				+ ", nombreNino=" + nombreNino + ", monster=" + monster + ", puerta=" + puerta + "]";
	}
}
