package data;

import java.util.LinkedList;

import entities.Puesto;

import java.sql.*;

public class DataPuesto {

	public LinkedList<Puesto> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Puesto> puestos = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from puesto");

			if (rs!=null) {
				while(rs.next()) {
					Puesto p = new Puesto();
					p.setId(rs.getInt("id"));
					p.setNombre(rs.getString("nombre"));
					p.setDescripcion(rs.getString("descripcion"));
					
					puestos.add(p);
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
		return puestos;
	}
	
	public Puesto GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Puesto puesto = new Puesto();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from puesto where id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					Puesto p = new Puesto();
					p.setId(rs.getInt("id"));
					p.setNombre(rs.getString("nombre"));
					p.setDescripcion(rs.getString("descripcion"));
					
					puesto = p;
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return puesto;
	}
	
	public void Add(Puesto p) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into puesto (nombre, descripcion) values (?, ?)");
			pstmt.setString(1, p.getNombre());
			pstmt.setString(2, p.getDescripcion());
			pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Update(Puesto p) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update puesto set nombre = ?, descripcion = ? where id = ?");
			pstmt.setString(1, p.getNombre());
			pstmt.setString(2, p.getDescripcion());
			pstmt.setInt(3, p.getId());
			pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Delete(int id) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from puesto where id = ?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
