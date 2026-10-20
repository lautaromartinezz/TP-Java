package dto;

public class AsignacionMonstruoDTO {
	private int idAsignacion;
    private String numeroPuerta;
    private String dniNino; 
    private String nombrePuesto;
    private Integer energiaGenerada; 
    private String estado; 
    private java.sql.Timestamp fecha;

    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int idAsignacion) { this.idAsignacion = idAsignacion; }
    public java.sql.Timestamp getFecha() { return fecha; }
    public void setFecha(java.sql.Timestamp fecha) { this.fecha = fecha; }

    public String getNumeroPuerta() { return numeroPuerta; }
    public void setNumeroPuerta(String numeroPuerta) { this.numeroPuerta = numeroPuerta; }

    public String getDniNino() { return dniNino; }
    public void setDniNino(String dniNino) { this.dniNino = dniNino; }

    public String getNombrePuesto() { return nombrePuesto; }
    public void setNombrePuesto(String nombrePuesto) { this.nombrePuesto = nombrePuesto; }

    public Integer getEnergiaGenerada() { return energiaGenerada; }
    public void setEnergiaGenerada(Integer energiaGenerada) { this.energiaGenerada = energiaGenerada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
