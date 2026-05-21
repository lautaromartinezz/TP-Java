package controller;

import java.util.LinkedList;

import data.DataMonster;
import entities.Monster;

public class ControllerMonster {
	DataMonster dm = new DataMonster();
	
	public LinkedList<Monster> GetAll(){
		return dm.GetAll();
	}
}
