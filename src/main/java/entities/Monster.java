package entities;

public class Monster {
	private int id;
	private String nombre;
	private int energiaTotal;
	
	
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
	public int getEnergiaTotal() {
		return energiaTotal;
	}
	public void setEnergiaTotal(int energiaTotal) {
		this.energiaTotal = energiaTotal;
	}
	
	
	@Override
	public String toString() {
		return "\nMonster [id=" + id + ", nombre=" + nombre + ", energiaTotal=" + energiaTotal + "]";
	}
}
