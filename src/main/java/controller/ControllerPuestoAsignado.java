package controller;

import java.sql.Date;
import java.util.LinkedList;

import data.DataPuestoAsignado;
import entities.PuestoAsignado;

public class ControllerPuestoAsignado {
	DataPuestoAsignado dpa = new DataPuestoAsignado();
	
	public LinkedList<PuestoAsignado> GetAll(){
		return dpa.GetAll();
	}
	
	public PuestoAsignado GetOne(int id){
		return dpa.GetOne(id);
	}
	public int UpdateMonstruo(PuestoAsignado pa) {
		return dpa.UpdateMonstruo(pa);
	}

	public boolean AsignacionValida(PuestoAsignado pa) {
		return !dpa.ExistsMonsterOnDate(pa.getMonster().getId(), pa.getFecha(), pa.getId());
	}

	public void Add(PuestoAsignado pa) {
		dpa.Add(pa);
	}
	
	public void Update(PuestoAsignado pa) {
		dpa.Update(pa);
	}
	
	public void Delete(int id) {
		dpa.Delete(id);
	}

	public java.util.HashMap<Integer, Integer> getMonstruosDelDia(java.sql.Date fecha) {
		return dpa.getMonstruosDelDia(fecha); // dpa o como se llame tu variable Data
	}

	public java.util.HashMap<Integer, java.util.LinkedList<entities.Puerta>> getPuertasDelDia(java.sql.Date fecha) {
		return dpa.getPuertasDelDia(fecha);
	}
}
