package entities;

public class Puesto {
	private int id;
	private String nombre;
	private String descripcion;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "\nPuesto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + "]";
	}
}
