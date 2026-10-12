package controller;

import java.util.LinkedList;
import java.util.Random;

import data.DataResultado;
import entities.Resultado;

public class ControllerResultado {
	DataResultado dr = new DataResultado();
	private Random random = new Random();
	
	public LinkedList<Resultado> GetAll(){
		return dr.GetAll();
	}
	
	public Resultado GetOne(int id){
		return dr.GetOne(id);
	}

	public void Add(Resultado r) {
		if (r.getEnergiaGenerada() <= 0) {
			r.setEnergiaGenerada(GenerarEnergia());
		}
		dr.Add(r);
	}
	
	public int GenerarEnergia() {
		return 100 + random.nextInt(1901);
	}
	
	public void Update(Resultado r) {
		dr.Update(r);
	}
	
	public void Delete(int id) {
		dr.Delete(id);
	}
}
