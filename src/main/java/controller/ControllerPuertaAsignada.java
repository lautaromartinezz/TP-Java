package controller;

import java.util.LinkedList;

import data.DataPuertaAsignada;
import entities.PuertaAsignada;

public class ControllerPuertaAsignada {
	DataPuertaAsignada dda = new DataPuertaAsignada();
	
	public LinkedList<PuertaAsignada> GetAll(){
		return dda.GetAll();
	}
	
	public LinkedList<PuertaAsignada> GetByPuestoAsignado(int idPuestoAsignado){
		return dda.GetByPuestoAsignado(idPuestoAsignado);
	}
	
	public PuertaAsignada GetOne(int id){
		return dda.GetOne(id);
	}

	public boolean AsignacionValida(PuertaAsignada da) {
		return !dda.Exists(da.getPuestoAsignado().getId(), da.getPuerta().getId(), da.getId());
	}

	public void Add(PuertaAsignada da) {
		dda.Add(da);
	}
	
	public void Update(PuertaAsignada da) {
		dda.Update(da);
	}
	
	public void Delete(int id) {
		dda.Delete(id);
	}
}
