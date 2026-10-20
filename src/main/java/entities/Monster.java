package entities;

public class Monster {
	private int id;
	private String nombre;
	private int energiaTotal;

	private int legajo;
    private String password;
    private String rol;
	
    
	public int getLegajo() {
		return legajo;
	}
	public void setLegajo(int legajo) {
		this.legajo = legajo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}


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
