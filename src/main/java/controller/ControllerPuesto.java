package controller;

import java.util.LinkedList;

import data.DataPuesto;
import entities.Puesto;

public class ControllerPuesto {
	DataPuesto dp = new DataPuesto();
	
	public LinkedList<Puesto> GetAll(){
		return dp.GetAll();
	}
	
	public Puesto GetOne(int id){
		return dp.GetOne(id);
	}

	public void Add(Puesto p) {
		dp.Add(p);
	}
	
	public void Update(Puesto p) {
		dp.Update(p);
	}
	
	public void Delete(int id) {
		dp.Delete(id);
	}
}
