package controller;

import java.util.LinkedList;

import data.DataPuerta;
import entities.Puerta;

public class ControllerPuerta {
	DataPuerta dp = new DataPuerta();
	
	public LinkedList<Puerta> GetAll(){
		return dp.GetAll();
	}
	
	public Puerta GetOne(int id){
		return dp.GetOne(id);
	}

	public void Add(Puerta p) {
		dp.Add(p);
	}
	
	public void Update(Puerta p) {
		dp.Update(p);
	}
	
	public void Delete(int id) {
		dp.Delete(id);
	}
}
