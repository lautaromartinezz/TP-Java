package data;

import java.util.LinkedList;

import entities.Puerta;

import java.sql.*;

public class DataPuerta {

	private Puerta Map(ResultSet rs) throws SQLException {
		Puerta p = new Puerta();
		p.setId(rs.getInt("id"));
		p.setNumero(rs.getString("numero"));
		return p;
	}

	public LinkedList<Puerta> GetAll() {
		Statement stmt=null;
		ResultSet rs=null;
		LinkedList<Puerta> puertas = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from puerta order by numero");

			if (rs!=null) {
				while(rs.next()) {
					puertas.add(Map(rs));
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
		return puertas;
	}
	
	public Puerta GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Puerta puerta = new Puerta();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from puerta where id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					puerta = Map(rs);
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
		return puerta;
	}
	
	public void Add(Puerta p) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into puerta (numero) values (?)");
			pstmt.setString(1, p.getNumero());
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
	
	public void Update(Puerta p) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update puerta set numero = ? where id = ?");
			pstmt.setString(1, p.getNumero());
			pstmt.setInt(2, p.getId());
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
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from puerta where id = ?");
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
