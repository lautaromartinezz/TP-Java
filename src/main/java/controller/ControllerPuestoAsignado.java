package controller;

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
}
