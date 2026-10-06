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
	public Monster GetOne(int id) {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Monster mon = new Monster();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from monster where id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs!=null) {
				while(rs.next()) {
					Monster m = new Monster();
					m.setId(rs.getInt("id"));
					m.setName(rs.getString("name"));
					
					mon = m;
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
		return mon;
	}
	
	public void Add(Monster m) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into monster (name) values (?)");
			pstmt.setString(1, m.getName());
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
	
	public void Update(Monster m) {
		PreparedStatement pstmt=null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update monster set name = ? where id = ?");
			pstmt.setString(1, m.getName());
			pstmt.setInt(2, m.getId());
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
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from monster where id = ?");
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