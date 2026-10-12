package controller;

import java.util.LinkedList;

import data.DataMonster;
import entities.Monster;

public class ControllerMonster {
	DataMonster dm = new DataMonster();
	
	public LinkedList<Monster> GetAll(){
		return dm.GetAll();
	}
	
	public LinkedList<Monster> GetRanking(){
		return dm.GetRanking();
	}
	
	public Monster GetOne(int id){
		return dm.GetOne(id);
	}

	public void Add(Monster m) {
		dm.Add(m);
	}
	
	public void Update(Monster m) {
		dm.Update(m);
	}
	
	public void Delete(int id) {
		dm.Delete(id);
	}
}
