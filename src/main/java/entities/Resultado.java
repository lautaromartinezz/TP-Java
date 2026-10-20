package entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Resultado {
	private int id;
	private Date fecha;
	private int energiaGenerada;
	private String nombreNino;
	private PuertaAsignada puertaAsignada;

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
	public PuertaAsignada getPuertaAsignada() {
		return puertaAsignada;
	}
	public void setPuertaAsignada(PuertaAsignada puertaAsignada) {
		this.puertaAsignada = puertaAsignada;
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
				+ ", nombreNino=" + nombreNino + ", puertaAsignada=" + puertaAsignada + "]";
	}
}
