package controller;

import data.DataMiedo;
import entities.Miedo;
import java.util.LinkedList;

public class ControllerMiedo {
    
    private DataMiedo dm = new DataMiedo();

    public void Add(Miedo m) {
        dm.Add(m);
    }

    public LinkedList<Miedo> GetAll() {
        return dm.GetAll();
    }

    public Miedo GetOne(int id) {
        return dm.GetOne(id);
    }

    public void Update(Miedo m) {
        dm.Update(m);
    }

    public void Delete(int id) {
        dm.Delete(id);
    }
    
}