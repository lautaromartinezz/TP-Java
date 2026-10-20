package controller;
import dto.AsignacionMonstruoDTO;
import java.util.List;
import data.DataPuertaAsignada;
import java.util.LinkedList;
import entities.PuertaAsignada;

public class ControllerPuertaAsignada {
	DataPuertaAsignada dda = new DataPuertaAsignada();
	
	public LinkedList<PuertaAsignada> GetAll(){
		return dda.GetAll();
	}
	public int contarPuertasPendientes(int idMonster) {
	    return new DataPuertaAsignada().contarPuertasPendientes(idMonster);
	}
	public List<AsignacionMonstruoDTO> getAllAsignaciones(int idMonster) {
	    return new data.DataPuertaAsignada().getAllAsignaciones(idMonster);
	}

	public List<AsignacionMonstruoDTO> getTop3Pendientes(int idMonster) {
	    return new DataPuertaAsignada().getTop3Pendientes(idMonster);
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
	public void DeleteByPuestoAsignado(int idPuestoAsignado) {
		dda.DeleteByPuestoAsignado(idPuestoAsignado); // Usa tu variable instanciada
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
