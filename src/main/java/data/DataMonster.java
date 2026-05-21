package data;

import java.util.LinkedList;

import entities.Monster;

import java.sql.*;

public class DataMonster {
	
	
	public LinkedList<Monster> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Monster> mons = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from monster");

			if (rs!=null) {
				while(rs.next()) {
					Monster m = new Monster();
					m.setId(rs.getInt("id"));
					m.setName(rs.getString("name"));
					
					mons.add(m);
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mons;
	}
}
	
	